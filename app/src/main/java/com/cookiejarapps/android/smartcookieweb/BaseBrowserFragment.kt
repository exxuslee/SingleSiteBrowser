package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import androidx.annotation.CallSuper
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cookiejarapps.android.smartcookieweb.*
import com.cookiejarapps.android.smartcookieweb.browser.BrowsingMode
import com.cookiejarapps.android.smartcookieweb.browser.HomepageChoice
import com.cookiejarapps.android.smartcookieweb.databinding.FragmentBrowserBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import com.cookiejarapps.android.smartcookieweb.integration.ContextMenuIntegration
import com.cookiejarapps.android.smartcookieweb.integration.FindInPageIntegration
import com.cookiejarapps.android.smartcookieweb.integration.ReaderModeIntegration
import com.cookiejarapps.android.smartcookieweb.preferences.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import mozilla.components.browser.state.action.ContentAction
import mozilla.components.browser.state.selector.*
import mozilla.components.browser.state.state.CustomTabSessionState
import mozilla.components.browser.state.state.SessionState
import mozilla.components.browser.state.state.TabSessionState
import mozilla.components.browser.state.state.content.DownloadState
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.browser.thumbnails.BrowserThumbnails
import mozilla.components.feature.app.links.AppLinksFeature
import mozilla.components.feature.downloads.DownloadsFeature
import mozilla.components.feature.intent.ext.EXTRA_SESSION_ID
import mozilla.components.feature.media.fullscreen.MediaSessionFullscreenFeature
import mozilla.components.feature.prompts.PromptFeature
import mozilla.components.feature.search.SearchFeature
import mozilla.components.feature.session.FullScreenFeature
import mozilla.components.feature.session.PictureInPictureFeature
import mozilla.components.feature.session.SessionFeature
import mozilla.components.feature.session.SwipeRefreshFeature
import mozilla.components.feature.sitepermissions.SitePermissionsFeature
import mozilla.components.lib.state.ext.consumeFlow
import mozilla.components.lib.state.ext.flowScoped
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.PermissionsFeature
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.base.feature.ViewBoundFeatureWrapper
import mozilla.components.support.ktx.android.view.exitImmersiveMode
import mozilla.components.support.ktx.kotlinx.coroutines.flow.ifAnyChanged
import mozilla.components.support.ktx.kotlinx.coroutines.flow.ifChanged
import java.lang.ref.WeakReference

/**
 * Base fragment extended by [BrowserFragment].
 * This class only contains shared code focused on the main browsing content.
 * UI code specific to the app or to custom tabs can be found in the subclasses.
 */
@ExperimentalCoroutinesApi
@Suppress("TooManyFunctions", "LargeClass")
abstract class BaseBrowserFragment : Fragment(), UserInteractionHandler, ActivityResultHandler, AccessibilityManager.AccessibilityStateChangeListener {

    private lateinit var browserAnimator: BrowserAnimator

    protected val thumbnailsFeature = ViewBoundFeatureWrapper<BrowserThumbnails>()

    private val sessionFeature = ViewBoundFeatureWrapper<SessionFeature>()
    private val contextMenuIntegration = ViewBoundFeatureWrapper<ContextMenuIntegration>()
    private val downloadsFeature = ViewBoundFeatureWrapper<DownloadsFeature>()
    private val appLinksFeature = ViewBoundFeatureWrapper<AppLinksFeature>()
    private val promptsFeature = ViewBoundFeatureWrapper<PromptFeature>()
    private val findInPageIntegration = ViewBoundFeatureWrapper<FindInPageIntegration>()
    private val sitePermissionsFeature = ViewBoundFeatureWrapper<SitePermissionsFeature>()
    private val fullScreenFeature = ViewBoundFeatureWrapper<FullScreenFeature>()
    private val swipeRefreshFeature = ViewBoundFeatureWrapper<SwipeRefreshFeature>()
    private var fullScreenMediaSessionFeature = ViewBoundFeatureWrapper<MediaSessionFullscreenFeature>()
    private val searchFeature = ViewBoundFeatureWrapper<SearchFeature>()
    private var pipFeature: PictureInPictureFeature? = null
    val readerViewFeature = ViewBoundFeatureWrapper<ReaderModeIntegration>()

    var customTabSessionId: String? = null

    @VisibleForTesting
    internal var browserInitialized: Boolean = false
    private var initUIJob: Job? = null
    protected var webAppToolbarShouldBeVisible = true

    private var _binding: FragmentBrowserBinding? = null
    protected val binding get() = _binding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        customTabSessionId = requireArguments().getString(EXTRA_SESSION_ID)
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            initializeUI(view)

            if (customTabSessionId == null) {
                // We currently only need this observer to navigate to home
                // in case all tabs have been removed on startup. No need to
                // this if we have a known session to display.
                observeRestoreComplete(requireContext().components.store, findNavController())
            }
        }

    private fun initializeUI(view: View) {
        val tab = getCurrentTab()
        browserInitialized = if (tab != null) {
            initializeUI(view, tab)
            true
        } else {
            false
        }
    }

    @Suppress("ComplexMethod", "LongMethod")
    @CallSuper
    internal open fun initializeUI(view: View, tab: SessionState) {
        val context = requireContext()
        val store = context.components.store
        val activity = requireActivity() as BrowserActivity

        val toolbarHeight = resources.getDimensionPixelSize(R.dimen.browser_toolbar_height)

        browserAnimator = BrowserAnimator(
            fragment = WeakReference(this),
            engineView = WeakReference(binding.engineView),
            swipeRefresh = WeakReference(binding.swipeRefresh),
            viewLifecycleScope = WeakReference(viewLifecycleOwner.lifecycleScope)
        ).apply {
            beginAnimateInIfNecessary()
        }

        val openInFenixIntent = Intent(context, IntentReceiverActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(BrowserActivity.OPEN_TO_BROWSER, true)
        }

        findInPageIntegration.set(
            feature = FindInPageIntegration(
                store = store,
                sessionId = customTabSessionId,
                stub = binding.stubFindInPage,
                engineView = binding.engineView,
            ),
            owner = this,
            view = view
        )

        contextMenuIntegration.set(
            feature = ContextMenuIntegration(
                context = requireContext(),
                fragmentManager = parentFragmentManager,
                browserStore = components.store,
                tabsUseCases = components.tabsUseCases,
                contextMenuUseCases = components.contextMenuUseCases,
                parentView = view,
                sessionId = customTabSessionId
            ),
            owner = this,
            view = view
        )

        readerViewFeature.set(
            feature = ReaderModeIntegration(
                requireContext(),
                components.engine,
                components.store,
                binding.readerViewBar,
                binding.readerViewAppearanceButton
            ),
            owner = this,
            view = view
        )

        promptsFeature.set(
                feature = PromptFeature(
                        activity = activity,
                        store = components.store,
                        customTabId = customTabSessionId,
                        fragmentManager = parentFragmentManager,
                        onNeedToRequestPermissions = { permissions ->
                            requestPermissions(permissions, REQUEST_CODE_PROMPT_PERMISSIONS)
                        }),
                owner = this,
                view = view
        )

        fullScreenMediaSessionFeature.set(
            feature = MediaSessionFullscreenFeature(
                requireActivity(),
                context.components.store,
                customTabSessionId
            ),
            owner = this,
            view = view
        )

        pipFeature = PictureInPictureFeature(
            store = store,
            activity = requireActivity(),
            tabId = customTabSessionId
        )

        appLinksFeature.set(
            feature = AppLinksFeature(
                context,
                store = store,
                sessionId = customTabSessionId,
                fragmentManager = parentFragmentManager,
                launchInApp = { UserPreferences(context).launchInApp },
                loadUrlUseCase = context.components.sessionUseCases.loadUrl
            ),
            owner = this,
            view = view
        )

        sessionFeature.set(
            feature = SessionFeature(
                requireContext().components.store,
                requireContext().components.sessionUseCases.goBack,
                binding.engineView,
                customTabSessionId
            ),
            owner = this,
            view = view
        )

        searchFeature.set(
            feature = SearchFeature(store, customTabSessionId) { request, tabId ->
                val parentSession = store.state.findTabOrCustomTab(tabId)
                val useCase = if (request.isPrivate) {
                    requireContext().components.searchUseCases.newPrivateTabSearch
                } else {
                    requireContext().components.searchUseCases.newTabSearch
                }

                if (parentSession is CustomTabSessionState) {
                    useCase.invoke(request.query)
                    requireActivity().startActivity(openInFenixIntent)
                } else {
                    useCase.invoke(request.query, parentSessionId = parentSession?.id)
                }
            },
            owner = this,
            view = view
        )

        val accentHighContrastColor = R.color.secondary_icon

        sitePermissionsFeature.set(
            feature = SitePermissionsFeature(
                context = context,
                storage = context.components.permissionStorage,
                fragmentManager = parentFragmentManager,
                promptsStyling = SitePermissionsFeature.PromptsStyling(
                    gravity = 0,
                    shouldWidthMatchParent = true,
                    positiveButtonBackgroundColor = accentHighContrastColor,
                    positiveButtonTextColor = R.color.photonWhite
                ),
                sessionId = customTabSessionId,
                onNeedToRequestPermissions = { permissions ->
                    requestPermissions(permissions, REQUEST_CODE_APP_PERMISSIONS)
                },
                onShouldShowRequestPermissionRationale = {
                    shouldShowRequestPermissionRationale(
                        it
                    )
                },
                store = store
            ),
            owner = this,
            view = view
        )

        fullScreenFeature.set(
            feature = FullScreenFeature(
                requireContext().components.store,
                requireContext().components.sessionUseCases,
                customTabSessionId,
                ::viewportFitChange,
                ::fullScreenChanged
            ),
            owner = this,
            view = view
        )

        expandToolbarOnNavigation(store)

        store.flowScoped(viewLifecycleOwner) { flow ->
            flow.mapNotNull { state -> state.findTabOrCustomTabOrSelectedTab(customTabSessionId) }
                .ifChanged { tab -> tab.content.pictureInPictureEnabled }
                .collect { tab -> pipModeChanged(tab) }
        }

        binding.swipeRefresh.isEnabled = shouldPullToRefreshBeEnabled(false)

        if (binding.swipeRefresh.isEnabled) {
            val primaryTextColor = ContextCompat.getColor(context, R.color.primary_icon)
            binding.swipeRefresh.setColorSchemeColors(primaryTextColor)
            swipeRefreshFeature.set(
                feature = SwipeRefreshFeature(
                    requireContext().components.store,
                    context.components.sessionUseCases.reload,
                    binding.swipeRefresh,
                    customTabSessionId
                ),
                owner = this,
                view = view
            )
        }

        initializeEngineView(toolbarHeight)
    }

    @VisibleForTesting
    internal fun expandToolbarOnNavigation(store: BrowserStore) {
        consumeFlow(store) { flow ->
            flow.mapNotNull {
                    state -> state.findCustomTabOrSelectedTab(customTabSessionId)
            }
                .ifAnyChanged {
                        tab -> arrayOf(tab.content.url, tab.content.loadRequest)
                }
                .collect {
                    findInPageIntegration.onBackPressed()
                }
        }
    }

    @VisibleForTesting
    internal fun shouldPullToRefreshBeEnabled(inFullScreen: Boolean): Boolean {
        return UserPreferences(requireContext()).swipeToRefresh
    }

    @VisibleForTesting
    internal fun initializeEngineView(toolbarHeight: Int) {
        val context = requireContext()

        if (UserPreferences(context).hideBarWhileScrolling) {
            binding.engineView.setDynamicToolbarMaxHeight(toolbarHeight)
        } else {
            binding.engineView.setDynamicToolbarMaxHeight(0)

            val swipeRefreshParams =
                binding.swipeRefresh.layoutParams as CoordinatorLayout.LayoutParams
                swipeRefreshParams.bottomMargin = toolbarHeight

        }
    }

    //TODO: Custom add-on collections, final clean up, VideoDL + update SCW/BB

    @VisibleForTesting
    internal fun observeRestoreComplete(store: BrowserStore, navController: NavController) {
        val activity = activity as BrowserActivity
        consumeFlow(store) { flow ->
            flow.map { state -> state.restoreComplete }
                .ifChanged()
                .collect { restored ->
                    if (restored) {
                        val tabs =
                            store.state.getNormalOrPrivateTabs(
                                activity.browsingModeManager.mode.isPrivate
                            )
                        if (tabs.isEmpty() || store.state.selectedTabId == null) {
                            when(UserPreferences(requireContext()).homepageType){
                                HomepageChoice.VIEW.ordinal -> {      }
                                HomepageChoice.BLANK_PAGE.ordinal -> {
                                    components.tabsUseCases.addTab.invoke(
                                        "about:blank",
                                        selectTab = true
                                    )
                                }
                                HomepageChoice.CUSTOM_PAGE.ordinal -> {
                                    components.tabsUseCases.addTab.invoke(
                                        UserPreferences(requireContext()).customHomepageUrl,
                                        selectTab = true
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        val components = requireContext().components

        val preferredColorScheme = components.darkEnabled()
        if (components.engine.settings.preferredColorScheme != preferredColorScheme) {
            components.engine.settings.preferredColorScheme = preferredColorScheme
            components.sessionUseCases.reload()
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        components.store.state.findTabOrCustomTabOrSelectedTab(customTabSessionId)?.let {
            updateThemeForSession(it)
        }
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        initUIJob?.cancel()

        requireContext().components.store.state.findTabOrCustomTabOrSelectedTab(customTabSessionId)
            ?.let { session ->
                // If we didn't enter PiP, exit full screen on stop
                if (!session.content.pictureInPictureEnabled && fullScreenFeature.onBackPressed()) {
                    fullScreenChanged(false)
                }
            }
    }

    @CallSuper
    override fun onBackPressed(): Boolean {
        return readerViewFeature.onBackPressed() ||
                findInPageIntegration.onBackPressed() ||
                fullScreenFeature.onBackPressed() ||
                promptsFeature.onBackPressed() ||
                sessionFeature.onBackPressed() ||
                removeSessionIfNeeded()
    }

    /**
     * Saves the external app session ID to be restored later in [onViewStateRestored].
     */
    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CUSTOM_TAB_SESSION_ID, customTabSessionId)
    }

    /**
     * Retrieves the external app session ID saved by [onSaveInstanceState].
     */
    final override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString(KEY_CUSTOM_TAB_SESSION_ID)?.let {
            if (requireContext().components.store.state.findCustomTab(it) != null) {
                customTabSessionId = it
            }
        }
    }

    /**
     * Forwards permission grant results to one of the features.
     */
    final override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val feature: PermissionsFeature? = when (requestCode) {
            REQUEST_CODE_DOWNLOAD_PERMISSIONS -> downloadsFeature.get()
            REQUEST_CODE_PROMPT_PERMISSIONS -> promptsFeature.get()
            REQUEST_CODE_APP_PERMISSIONS -> sitePermissionsFeature.get()
            else -> null
        }
        feature?.onPermissionsResult(permissions, grantResults)
    }

    /**
     * Forwards activity results to the [ActivityResultHandler] features.
     */
    override fun onActivityResult(requestCode: Int, data: Intent?, resultCode: Int): Boolean {
        return listOf(
            promptsFeature
        ).any { it.onActivityResult(requestCode, data, resultCode) }
    }

    /**
     * Removes the session if it was opened by an ACTION_VIEW intent
     * or if it has a parent session and no more history
     */
    protected open fun removeSessionIfNeeded(): Boolean {
        getCurrentTab()?.let { session ->
            return if (session.source is SessionState.Source.External && !session.restored) {
                activity?.finish()
                requireContext().components.tabsUseCases.removeTab(session.id)
                true
            } else {
                val hasParentSession = session is TabSessionState && session.parentId != null
                if (hasParentSession) {
                    requireContext().components.tabsUseCases.removeTab(session.id, selectParentIfExists = true)
                }
                // We want to return to home if this session didn't have a parent session to select.
                val goToOverview = !hasParentSession
                !goToOverview
            }
        }
        return false
    }

    /**
     * Set the activity normal/private theme to match the current session.
     */
    @VisibleForTesting
    internal fun updateThemeForSession(session: SessionState) {
        val sessionMode = BrowsingMode.fromBoolean(session.content.private)
        (activity as BrowserActivity).browsingModeManager.mode = sessionMode
    }

    @VisibleForTesting
    internal fun getCurrentTab(): SessionState? {
        return requireContext().components.store.state.findCustomTabOrSelectedTab(customTabSessionId)
    }

    override fun onHomePressed() = pipFeature?.onHomePressed() ?: false

    /**
     * Exit fullscreen mode when exiting PIP mode
     */
    private fun pipModeChanged(session: SessionState) {
        if (!session.content.pictureInPictureEnabled && session.content.fullScreen) {
            onBackPressed()
            fullScreenChanged(false)
        }
    }

    final override fun onPictureInPictureModeChanged(enabled: Boolean) {
        pipFeature?.onPictureInPictureModeChanged(enabled)
    }

    private fun viewportFitChange(layoutInDisplayCutoutMode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val layoutParams = activity?.window?.attributes
            layoutParams?.layoutInDisplayCutoutMode = layoutInDisplayCutoutMode
            activity?.window?.attributes = layoutParams
        }
    }

    @VisibleForTesting
    internal fun fullScreenChanged(inFullScreen: Boolean) {
        if (inFullScreen) {
            // Close find in page bar if opened
            findInPageIntegration.onBackPressed()

            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            val browserEngine = binding.swipeRefresh.layoutParams as CoordinatorLayout.LayoutParams
            browserEngine.bottomMargin = 0
            browserEngine.topMargin = 0
            binding.swipeRefresh.translationY = 0f

            binding.engineView.setDynamicToolbarMaxHeight(0)
            // Without this, fullscreen has a margin at the top.
            binding.engineView.setVerticalClipping(0)

        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            requireActivity().window.getDecorView().systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

            activity?.exitImmersiveMode()
            if (webAppToolbarShouldBeVisible) {
                val toolbarHeight = resources.getDimensionPixelSize(R.dimen.browser_toolbar_height)
                initializeEngineView(toolbarHeight)
            }
        }

        binding.swipeRefresh?.isEnabled = shouldPullToRefreshBeEnabled(inFullScreen)
    }

    /*
     * Dereference these views when the fragment view is destroyed to prevent memory leaks
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_CUSTOM_TAB_SESSION_ID = "custom_tab_session_id"
        private const val REQUEST_CODE_DOWNLOAD_PERMISSIONS = 1
        private const val REQUEST_CODE_PROMPT_PERMISSIONS = 2
        private const val REQUEST_CODE_APP_PERMISSIONS = 3
    }

    override fun onAccessibilityStateChanged(enabled: Boolean) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}