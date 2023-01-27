package com.exzi.android.components

import android.content.Context
import android.content.SharedPreferences
import com.exzi.android.BrowserActivity
import com.exzi.android.ext.components
import com.exzi.android.preferences.UserPreferences
import com.exzi.android.request.AppRequestInterceptor
import com.exzi.android.utils.ClipboardHandler
import com.exzi.android.R
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.engine.gecko.ext.toContentBlockingSetting
import mozilla.components.browser.engine.gecko.fetch.GeckoViewFetchClient
import mozilla.components.browser.engine.gecko.permission.GeckoSitePermissionsStorage
import mozilla.components.browser.icons.BrowserIcons
import mozilla.components.browser.state.engine.EngineMiddleware
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.browser.storage.sync.PlacesHistoryStorage
import mozilla.components.browser.thumbnails.ThumbnailsMiddleware
import mozilla.components.browser.thumbnails.storage.ThumbnailStorage
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.fetch.Client
import mozilla.components.feature.app.links.AppLinksInterceptor
import mozilla.components.feature.pwa.ManifestStorage
import mozilla.components.feature.pwa.WebAppInterceptor
import mozilla.components.feature.session.HistoryDelegate
import mozilla.components.feature.session.SessionUseCases
import mozilla.components.feature.session.middleware.LastAccessMiddleware
import mozilla.components.feature.session.middleware.undo.UndoMiddleware
import mozilla.components.feature.sitepermissions.OnDiskSitePermissionsStorage
import mozilla.components.feature.tabs.TabsUseCases
import mozilla.components.feature.webcompat.WebCompatFeature
import mozilla.components.feature.webnotifications.WebNotificationFeature
import mozilla.components.lib.publicsuffixlist.PublicSuffixList
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

@Suppress("LargeClass")
open class Components(private val applicationContext: Context) {
    companion object {
        const val BROWSER_PREFERENCES = "browser_preferences"
        const val PREF_LAUNCH_EXTERNAL_APP = "launch_external_app"
    }

    val publicSuffixList by lazy { PublicSuffixList(applicationContext) }

    val clipboardHandler by lazy { ClipboardHandler(applicationContext) }

    val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(BROWSER_PREFERENCES, Context.MODE_PRIVATE)

    val appRequestInterceptor by lazy {
        AppRequestInterceptor(applicationContext)
    }

    // Engine Settings
    val engineSettings by lazy {
        DefaultSettings().apply {
            historyTrackingDelegate = HistoryDelegate(lazyHistoryStorage)
            requestInterceptor = appRequestInterceptor
            remoteDebuggingEnabled = UserPreferences(applicationContext).remoteDebugging
            supportMultipleWindows = true
            enterpriseRootsEnabled = UserPreferences(applicationContext).trustThirdPartyCerts
            if(!UserPreferences(applicationContext).autoFontSize){
                fontSizeFactor = UserPreferences(applicationContext).fontSizeFactor
                automaticFontSizeAdjustment = false
            }
//            preferredColorScheme = darkEnabled()
            javascriptEnabled = UserPreferences(applicationContext).javaScriptEnabled
        }
    }

    // Engine
    open val engine: Engine by lazy {
        GeckoEngine(applicationContext, engineSettings, runtime).also {
            WebCompatFeature.install(it)
        }
    }

    open val client: Client by lazy { GeckoViewFetchClient(applicationContext, runtime) }

    val icons by lazy { BrowserIcons(applicationContext, client) }

    // Storage
    private val lazyHistoryStorage = lazy { PlacesHistoryStorage(applicationContext) }
    val permissionStorage by lazy { GeckoSitePermissionsStorage(runtime, OnDiskSitePermissionsStorage(applicationContext)) }
    val thumbnailStorage by lazy { ThumbnailStorage(applicationContext) }

    val store by lazy {
        BrowserStore(
                middleware = listOf(
//                        ReaderViewMiddleware(),
                        ThumbnailsMiddleware(thumbnailStorage),
                        UndoMiddleware(),
//                        RegionMiddleware(
//                                applicationContext,
//                                LocationService.default()
//                        ),
//                        SearchMiddleware(applicationContext),
//                        RecordingDevicesMiddleware(applicationContext),
                        LastAccessMiddleware()
                ) + EngineMiddleware.create(engine)
        ).apply{
            icons.install(engine, this)

            WebNotificationFeature(
                    applicationContext, engine, icons, R.drawable.ic_notification,
                    permissionStorage, BrowserActivity::class.java
            )
        }
    }

    val sessionUseCases by lazy { SessionUseCases(store) }
    val appLinksInterceptor by lazy {
        AppLinksInterceptor(
                applicationContext,
                interceptLinkClicks = true,
                launchInApp = {
                    applicationContext.components.preferences.getBoolean(
                            PREF_LAUNCH_EXTERNAL_APP,
                            false
                    )
                }
        )
    }

    val webAppInterceptor by lazy {
        WebAppInterceptor(
                applicationContext,
                webAppManifestStorage
        )
    }

    private val runtime by lazy {
        val builder = GeckoRuntimeSettings.Builder()

        val runtimeSettings = builder
            .aboutConfigEnabled(true)
            .contentBlocking(trackingPolicy.toContentBlockingSetting())
            .build()

        runtimeSettings.contentBlocking.setSafeBrowsing(safeBrowsingPolicy)

        if(UserPreferences(applicationContext).safeBrowsing){
            runtimeSettings.contentBlocking.setSafeBrowsingProviders(
                ContentBlocking.GOOGLE_SAFE_BROWSING_PROVIDER,
                ContentBlocking.GOOGLE_LEGACY_SAFE_BROWSING_PROVIDER
            )
            runtimeSettings.contentBlocking.setSafeBrowsingMalwareTable(
                "goog-malware-proto",
                "goog-unwanted-proto"
            )
            runtimeSettings.contentBlocking.setSafeBrowsingPhishingTable(
                "goog-phish-proto"
            )
        }
        else {
            runtimeSettings.contentBlocking.setSafeBrowsingProviders()
            runtimeSettings.contentBlocking.setSafeBrowsingMalwareTable()
            runtimeSettings.contentBlocking.setSafeBrowsingPhishingTable()
        }

        GeckoRuntime.create(applicationContext, runtimeSettings)
    }

    private val trackingPolicy by lazy{
        if(UserPreferences(applicationContext).trackingProtection) EngineSession.TrackingProtectionPolicy.recommended()
        else EngineSession.TrackingProtectionPolicy.none()
    }

    private val safeBrowsingPolicy by lazy{
        if(UserPreferences(applicationContext).safeBrowsing) ContentBlocking.SafeBrowsing.DEFAULT
        else ContentBlocking.SafeBrowsing.NONE
    }

    val webAppManifestStorage by lazy { ManifestStorage(applicationContext) }
    val tabsUseCases: TabsUseCases by lazy { TabsUseCases(store) }
}