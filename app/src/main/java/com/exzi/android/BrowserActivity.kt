package com.exzi.android

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.exzi.android.ext.components
import com.exzi.android.preferences.UserPreferences
import com.exzi.android.utils.PrintUtils
import com.exzi.android.browser.BrowsingMode
import com.exzi.android.browser.BrowsingModeManager
import com.exzi.android.browser.DefaultBrowsingModeManager
import com.exzi.android.databinding.ActivityMainBinding
import mozilla.components.browser.state.search.SearchEngine
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.SessionState
import mozilla.components.browser.state.state.WebExtensionState
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.EngineView
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.feature.contextmenu.ext.DefaultSelectionActionDelegate
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.ktx.kotlin.isUrl
import mozilla.components.support.ktx.kotlin.toNormalizedUrl
import mozilla.components.support.utils.SafeIntent
import mozilla.components.support.webextensions.WebExtensionPopupFeature


/**
 * Activity that holds the [BrowserFragment].
 */
open class BrowserActivity : AppCompatActivity(), ComponentCallbacks2 {

    lateinit var binding: ActivityMainBinding
    lateinit var browsingModeManager: BrowsingModeManager
    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
    }

    private val webExtensionPopupFeature by lazy {
        WebExtensionPopupFeature(components.store, ::openPopup)
    }

    private var mPort: Port? = null

    private var originalContext: Context? = null

    protected open fun getIntentSessionId(intent: SafeIntent): String? = null

    @VisibleForTesting
    internal fun isActivityColdStarted(startingIntent: Intent, activityIcicle: Bundle?): Boolean {
        return activityIcicle == null && startingIntent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY == 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        components.publicSuffixList.prefetch()
        browsingModeManager = createBrowsingModeManager(BrowsingMode.Normal)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        if (UserPreferences(this).firstLaunch) {
            UserPreferences(this).firstLaunch = false
        }
        //TODO: remove this once most people have updated
        if (UserPreferences(this).showTabsInGrid && UserPreferences(this).stackFromBottom) UserPreferences(
            this
        ).stackFromBottom = false

        //TODO: Move to settings page so app restart no longer required
        //TODO: Differentiate between using search engine / adding to list - the code below removes all from list as I don't support adding to list, only setting as default
        for (i in components.store.state.search.customSearchEngines) {
            components.searchUseCases.removeSearchEngine(i)
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        installPrintExtension()
        components.appRequestInterceptor.setNavController(navHost.navController)
        lifecycle.addObserver(webExtensionPopupFeature)
    }

//    open fun navigateToBrowserOnColdStart() {
//        if (!browsingModeManager.mode.isPrivate) {
//            openToBrowser(BrowserDirection.FromGlobal, null)
//        }
//    }

    protected open fun createBrowsingModeManager(initialMode: BrowsingMode): BrowsingModeManager {
        return DefaultBrowsingModeManager(initialMode, UserPreferences(this)) {}
    }

    final override fun onBackPressed() {
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach {
            if (it is UserInteractionHandler && it.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? =
        when (name) {
            EngineView::class.java.name -> components.engine.createView(context, attrs).apply {
                selectionActionDelegate = DefaultSelectionActionDelegate(
                    store = components.store,
                    context = context
                )
            }.asView()
            else -> super.onCreateView(parent, name, context, attrs)
        }

    final override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach {
            if (it is ActivityResultHandler && it.onActivityResult(requestCode, data, resultCode)) {
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openPopup(webExtensionState: WebExtensionState) {
        val fm: FragmentManager = supportFragmentManager
        val bundle = Bundle()
        bundle.putString("web_extension_id", webExtensionState.id)
        intent.putExtra("web_extension_name", webExtensionState.name)
    }

    @Suppress("LongParameterList")
    fun openToBrowserAndLoad(
        searchTermOrURL: String,
        newTab: Boolean,
        from: BrowserDirection,
        customTabSessionId: String? = null,
        engine: SearchEngine? = null,
        forceSearch: Boolean = false,
        flags: EngineSession.LoadUrlFlags = EngineSession.LoadUrlFlags.none()
    ) {
        //openToBrowser(from, customTabSessionId)
        load(searchTermOrURL, newTab, engine, forceSearch, flags)
    }

//    fun openToBrowser(from: BrowserDirection, customTabSessionId: String? = null) {
//        if (navHost.navController.alreadyOnDestination(R.id.browserFragment)) return
//        @IdRes val fragmentId = if (from.fragmentId != 0) from.fragmentId else null
//        val directions = getNavDirections(from, customTabSessionId)
//        if (directions != null) {
//            navHost.navController.nav(fragmentId, directions)
//        }
//    }
//
//    protected open fun getNavDirections(
//        from: BrowserDirection,
//        customTabSessionId: String?
//    ): NavDirections? = when (from) {
//        BrowserDirection.FromGlobal ->
//            NavGraphDirections.actionGlobalBrowser(customTabSessionId)
//    }

    private fun load(
        searchTermOrURL: String,
        newTab: Boolean,
        engine: SearchEngine?,
        forceSearch: Boolean,
        flags: EngineSession.LoadUrlFlags = EngineSession.LoadUrlFlags.none()
    ) {
        val mode = browsingModeManager.mode

        val loadUrlUseCase = if (newTab) {
            when (mode) {
                BrowsingMode.Private -> components.tabsUseCases.addPrivateTab
                BrowsingMode.Normal -> components.tabsUseCases.addTab
            }
        } else components.sessionUseCases.loadUrl

        if ((!forceSearch && searchTermOrURL.isUrl()) || engine == null) {
            loadUrlUseCase.invoke(searchTermOrURL.toNormalizedUrl(), flags)
        } else {
            if (newTab) {
                components.searchUseCases.newTabSearch
                    .invoke(
                        searchTermOrURL,
                        SessionState.Source.Internal.UserEntered,
                        true,
                        mode.isPrivate,
                        searchEngine = engine
                    )
            } else {
                components.searchUseCases.defaultSearch.invoke(searchTermOrURL, engine)
            }
        }
    }

    private fun installPrintExtension() {
        val messageHandler = object : MessageHandler {
            override fun onPortConnected(port: Port) {
                mPort = port
            }

            override fun onPortDisconnected(port: Port) {}

            override fun onPortMessage(message: Any, port: Port) {
                val converter = PrintUtils.instance
                converter!!.convert(
                    originalContext,
                    message.toString(),
                    components.store.state.selectedTab?.content?.url
                )
            }

            override fun onMessage(message: Any, source: EngineSession?) {
            }
        }

        components.engine.installWebExtension(
            "print-helper@cookiejarapps.com",
            "resource://android/assets/print/",
            onSuccess = { extension ->
                extension.registerBackgroundMessageHandler("print", messageHandler)
            }
        )
    }

    override fun attachBaseContext(base: Context) {
        this.originalContext = base
        super.attachBaseContext(base)
    }

    companion object {
        const val OPEN_TO_BROWSER = "open_to_browser"
    }
}
