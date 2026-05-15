package com.cookiejarapps.android.smartcookieweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cookiejarapps.android.smartcookieweb.databinding.FragmentBrowserBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import mozilla.components.browser.state.action.TabListAction
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.createTab
import mozilla.components.feature.app.links.AppLinksFeature
import mozilla.components.feature.prompts.PromptFeature
import mozilla.components.feature.session.SessionFeature
import mozilla.components.feature.session.SwipeRefreshFeature
import mozilla.components.feature.sitepermissions.SitePermissionsFeature
import mozilla.components.feature.tabs.TabsUseCases
import mozilla.components.lib.state.ext.consumeFlow
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.PermissionsFeature
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.base.feature.ViewBoundFeatureWrapper
import mozilla.components.support.ktx.android.view.hideKeyboard
import mozilla.components.support.utils.ext.requestInPlacePermissions
import android.content.Intent
import android.content.pm.PackageManager

@ExperimentalCoroutinesApi
class BrowserFragment : Fragment(), UserInteractionHandler, ActivityResultHandler {

    private val sessionFeature = ViewBoundFeatureWrapper<SessionFeature>()
    private val appLinksFeature = ViewBoundFeatureWrapper<AppLinksFeature>()
    private val promptsFeature = ViewBoundFeatureWrapper<PromptFeature>()
    private val sitePermissionsFeature = ViewBoundFeatureWrapper<SitePermissionsFeature>()
    private val swipeRefreshFeature = ViewBoundFeatureWrapper<SwipeRefreshFeature>()

    private var browserInitialized = false
    private var _binding: FragmentBrowserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val startUrl: String
        get() = arguments?.getString(ARG_URL)
            ?: (activity as? BrowserActivity)?.intent?.getStringExtra(BrowserActivity.EXTRA_URL)
            ?: BuildConfig.SITE_URL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val store = requireContext().components.store
        ensureTabLoadsUrl(startUrl)

        consumeFlow(store) { flow ->
            flow.mapNotNull { it.selectedTab }
                .distinctUntilChanged { old, new -> old.id == new.id }
                .collect { tab ->
                    if (!browserInitialized) {
                        initializeFeatures(view)
                        browserInitialized = true
                    }
                }
        }
    }

    fun loadUrl(url: String) {
        arguments = (arguments ?: Bundle()).apply {
            putString(ARG_URL, url)
        }
        ensureTabLoadsUrl(url)
    }

    private fun ensureTabLoadsUrl(url: String) {
        val store = requireContext().components.store
        val selectedTab = store.state.selectedTab
        if (selectedTab == null) {
            store.dispatch(
                TabListAction.AddTabAction(
                    createTab(url = url),
                    select = true,
                ),
            )
        } else {
            val currentUrl = selectedTab.content.url
            if (currentUrl.isBlank() || currentUrl == "about:blank" || currentUrl != url) {
                requireContext().components.sessionUseCases.loadUrl(url, selectedTab.id)
            }
        }
    }

    private fun initializeFeatures(view: View) {
        val context = requireContext()
        val components = context.components
        val store = components.store
        val tabsUseCases = TabsUseCases(store)

        promptsFeature.set(
            PromptFeature(
                fragment = this,
                store = store,
                tabsUseCases = tabsUseCases,
                fragmentManager = parentFragmentManager,
                fileUploadsDirCleaner = components.fileUploadsDirCleaner,
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
                fragmentManager = parentFragmentManager,
                launchInApp = { BuildConfig.LAUNCH_IN_APP },
                loadUrlUseCase = components.sessionUseCases.loadUrl,
            ),
            owner = this,
            view = view,
        )

        sessionFeature.set(
            feature = SessionFeature(
                store,
                components.sessionUseCases.goBack,
                components.sessionUseCases.goForward,
                binding.engineView,
            ),
            owner = this,
            view = view,
        )

        sitePermissionsFeature.set(
            feature = SitePermissionsFeature(
                context = context,
                storage = components.permissionStorage,
                fragmentManager = parentFragmentManager,
                promptsStyling = SitePermissionsFeature.PromptsStyling(
                    gravity = 0,
                    shouldWidthMatchParent = true,
                    positiveButtonBackgroundColor = R.color.secondary_icon,
                    positiveButtonTextColor = android.R.color.white,
                ),
                onNeedToRequestPermissions = { permissions ->
                    requestPermissions(permissions, REQUEST_CODE_APP_PERMISSIONS)
                },
                onShouldShowRequestPermissionRationale = { shouldShowRequestPermissionRationale(it) },
                store = store,
            ),
            owner = this,
            view = view,
        )

        if (BuildConfig.SWIPE_TO_REFRESH) {
            binding.swipeRefresh.isEnabled = true
            binding.swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.primary_icon),
            )
            swipeRefreshFeature.set(
                feature = SwipeRefreshFeature(
                    store,
                    components.sessionUseCases.reload,
                    binding.swipeRefresh,
                    ({}),
                ),
                owner = this,
                view = view,
            )
        } else {
            binding.swipeRefresh.isEnabled = false
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
    }

    override fun onPause() {
        super.onPause()
        view?.hideKeyboard()
    }

    override fun onBackPressed(): Boolean {
        return promptsFeature.onBackPressed() || sessionFeature.onBackPressed()
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

    override fun onDestroyView() {
        super.onDestroyView()
        browserInitialized = false
        binding.engineView.setActivityContext(null)
        _binding = null
    }

    companion object {
        const val ARG_URL = "url"

        private const val REQUEST_KEY_PROMPT_PERMISSIONS = "promptFeature"
        private const val REQUEST_CODE_PROMPT_PERMISSIONS = 2
        private const val REQUEST_CODE_APP_PERMISSIONS = 3
    }
}
