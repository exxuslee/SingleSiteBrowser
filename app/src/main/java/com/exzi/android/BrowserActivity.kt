package com.exzi.android

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.exzi.android.databinding.ActivityMainBinding
import com.exzi.android.ext.components
import com.exzi.android.preferences.UserPreferences
import com.exzi.android.utils.PrintUtils
import mozilla.components.browser.state.search.SearchEngine
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.WebExtensionState
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.EngineView
import mozilla.components.concept.engine.webextension.MessageHandler
import mozilla.components.concept.engine.webextension.Port
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.ktx.kotlin.toNormalizedUrl
import mozilla.components.support.webextensions.WebExtensionPopupFeature


/**
 * Activity that holds the [BrowserFragment].
 */
open class BrowserActivity : AppCompatActivity(), ComponentCallbacks2 {

    lateinit var binding: ActivityMainBinding
    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
    }
    private val webExtensionPopupFeature by lazy {
        WebExtensionPopupFeature(components.store, ::openPopup)
    }
    private var mPort: Port? = null
    private var originalContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        components.publicSuffixList.prefetch()

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        installPrintExtension()
        components.appRequestInterceptor.setNavController(navHost.navController)
        lifecycle.addObserver(webExtensionPopupFeature)
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
        parent: View?, name: String, context: Context, attrs: AttributeSet
    ): View? =
        when (name) {
            EngineView::class.java.name -> components.engine.createView(context, attrs).asView()
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
        val bundle = Bundle()
        bundle.putString("web_extension_id", webExtensionState.id)
        intent.putExtra("web_extension_name", webExtensionState.name)
    }

    @Suppress("LongParameterList")
    fun openToBrowserAndLoad(
        searchTermOrURL: String,
        flags: EngineSession.LoadUrlFlags = EngineSession.LoadUrlFlags.none()
    ) {
        load(searchTermOrURL, flags)
    }

    private fun load(
        searchTermOrURL: String,
        flags: EngineSession.LoadUrlFlags = EngineSession.LoadUrlFlags.none()
    ) {
        val loadUrlUseCase = components.sessionUseCases.loadUrl
        loadUrlUseCase.invoke(searchTermOrURL.toNormalizedUrl(), flags)
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
}
