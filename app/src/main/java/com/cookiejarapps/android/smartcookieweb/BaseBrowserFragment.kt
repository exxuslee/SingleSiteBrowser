package com.cookiejarapps.android.smartcookieweb

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cookiejarapps.android.smartcookieweb.databinding.FragmentBrowserBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import com.cookiejarapps.android.smartcookieweb.preferences.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import mozilla.components.browser.state.selector.findCustomTab
import mozilla.components.browser.state.selector.findCustomTabOrSelectedTab
import mozilla.components.browser.state.state.SessionState
import mozilla.components.browser.state.state.TabSessionState
import mozilla.components.feature.app.links.AppLinksFeature
import mozilla.components.feature.intent.ext.EXTRA_SESSION_ID
import mozilla.components.feature.prompts.PromptFeature
import mozilla.components.feature.session.SessionFeature
import mozilla.components.feature.session.SwipeRefreshFeature
import mozilla.components.feature.sitepermissions.SitePermissionsFeature
import mozilla.components.lib.state.ext.consumeFlow
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.PermissionsFeature
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.base.feature.ViewBoundFeatureWrapper
import mozilla.components.support.ktx.android.view.hideKeyboard
import mozilla.components.support.utils.ext.requestInPlacePermissions

@ExperimentalCoroutinesApi
abstract class BaseBrowserFragment : Fragment(), UserInteractionHandler, ActivityResultHandler {

    private val sessionFeature = ViewBoundFeatureWrapper<SessionFeature>()
    private val appLinksFeature = ViewBoundFeatureWrapper<AppLinksFeature>()
    private val promptsFeature = ViewBoundFeatureWrapper<PromptFeature>()
    private val sitePermissionsFeature = ViewBoundFeatureWrapper<SitePermissionsFeature>()
    private val swipeRefreshFeature = ViewBoundFeatureWrapper<SwipeRefreshFeature>()

    var customTabSessionId: String? = null

    private var browserInitialized = false

    private var _binding: FragmentBrowserBinding? = null
    protected val binding get() = _binding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        customTabSessionId = requireArguments().getString(EXTRA_SESSION_ID)
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val store = requireContext().components.store
        consumeFlow(store) { flow ->
            flow.mapNotNull { state ->
                state.findCustomTabOrSelectedTab(customTabSessionId)
            }
                .distinctUntilChanged { old, new -> old.id == new.id }
                .collect { tab ->
                    if (!browserInitialized) {
                        initializeUI(view, tab)
                        browserInitialized = true
                    }
                }
        }
    }

    @CallSuper
    protected open fun initializeUI(view: View, tab: SessionState) {
        val context = requireContext()
        val store = context.components.store

        promptsFeature.set(
            PromptFeature(
                fragment = this,
                store = store,
                tabsUseCases = context.components.tabsUseCases,
                fragmentManager = parentFragmentManager,
                fileUploadsDirCleaner = context.components.fileUploadsDirCleaner,
                onNeedToRequestPermissions = { permissions ->
                    requestInPlacePermissions(REQUEST_KEY_PROMPT_PERMISSIONS, permissions) { result ->
                        promptsFeature.get()?.onPermissionsResult(
                            result.keys.toTypedArray(),
                            result.values.map {
                                if (it) PackageManager.PERMISSION_GRANTED
                                else PackageManager.PERMISSION_DENIED
                            }.toIntArray(),
                        )
                    }
                },
            ),
            this,
            view,
        )

        appLinksFeature.set(
            feature = AppLinksFeature(
                context,
                store = store,
                sessionId = customTabSessionId,
                fragmentManager = parentFragmentManager,
                launchInApp = { UserPreferences(context).launchInApp },
                loadUrlUseCase = context.components.sessionUseCases.loadUrl,
            ),
            owner = this,
            view = view,
        )

        sessionFeature.set(
            feature = SessionFeature(
                store,
                context.components.sessionUseCases.goBack,
                context.components.sessionUseCases.goForward,
                binding.engineView,
                customTabSessionId,
            ),
            owner = this,
            view = view,
        )

        sitePermissionsFeature.set(
            feature = SitePermissionsFeature(
                context = context,
                storage = context.components.permissionStorage,
                fragmentManager = parentFragmentManager,
                promptsStyling = SitePermissionsFeature.PromptsStyling(
                    gravity = 0,
                    shouldWidthMatchParent = true,
                    positiveButtonBackgroundColor = R.color.secondary_icon,
                    positiveButtonTextColor = R.color.photonWhite,
                ),
                sessionId = customTabSessionId,
                onNeedToRequestPermissions = { permissions ->
                    requestPermissions(permissions, REQUEST_CODE_APP_PERMISSIONS)
                },
                onShouldShowRequestPermissionRationale = { shouldShowRequestPermissionRationale(it) },
                store = store,
            ),
            owner = this,
            view = view,
        )

        binding.swipeRefresh.isEnabled = UserPreferences(context).swipeToRefresh
        if (binding.swipeRefresh.isEnabled) {
            binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.primary_icon),
            )
            swipeRefreshFeature.set(
                feature = SwipeRefreshFeature(
                    store,
                    context.components.sessionUseCases.reload,
                    binding.swipeRefresh,
                    ({}),
                    customTabSessionId,
                ),
                owner = this,
                view = view,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val components = requireContext().components
        val preferredColorScheme = components.darkEnabled()
        if (components.engine.settings.preferredColorScheme != preferredColorScheme) {
            components.engine.settings.preferredColorScheme = preferredColorScheme
            components.sessionUseCases.reload()
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        view?.hideKeyboard()
    }

    override fun onBackPressed(): Boolean {
        return promptsFeature.onBackPressed() ||
            sessionFeature.onBackPressed() ||
            removeSessionIfNeeded()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CUSTOM_TAB_SESSION_ID, customTabSessionId)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString(KEY_CUSTOM_TAB_SESSION_ID)?.let { sessionId ->
            if (requireContext().components.store.state.findCustomTab(sessionId) != null) {
                customTabSessionId = sessionId
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        val feature: PermissionsFeature? = when (requestCode) {
            REQUEST_CODE_PROMPT_PERMISSIONS -> promptsFeature.get()
            REQUEST_CODE_APP_PERMISSIONS -> sitePermissionsFeature.get()
            else -> null
        }
        feature?.onPermissionsResult(permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, data: Intent?, resultCode: Int): Boolean {
        return promptsFeature.onActivityResult(requestCode, data, resultCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        promptsFeature.withFeature { it.onActivityResult(requestCode, data, resultCode) }
    }

    private fun removeSessionIfNeeded(): Boolean {
        val session = getCurrentTab() ?: return false
        return if (session.source is SessionState.Source.External && !session.restored) {
            activity?.finish()
            requireContext().components.tabsUseCases.removeTab(session.id)
            true
        } else {
            val hasParent = session is TabSessionState && session.parentId != null
            if (hasParent) {
                requireContext().components.tabsUseCases.removeTab(session.id, selectParentIfExists = true)
            }
            hasParent
        }
    }

    private fun getCurrentTab(): SessionState? {
        return requireContext().components.store.state.findCustomTabOrSelectedTab(customTabSessionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        browserInitialized = false
        binding.engineView.setActivityContext(null)
        _binding = null
    }

    companion object {
        private const val KEY_CUSTOM_TAB_SESSION_ID = "custom_tab_session_id"
        private const val REQUEST_KEY_PROMPT_PERMISSIONS = "promptFeature"
        private const val REQUEST_CODE_PROMPT_PERMISSIONS = 2
        private const val REQUEST_CODE_APP_PERMISSIONS = 3
    }
}
