package com.cookiejarapps.android.smartcookieweb.components

import android.content.Context
import android.content.res.Configuration
import com.cookiejarapps.android.smartcookieweb.BuildConfig
import com.cookiejarapps.android.smartcookieweb.request.AppRequestInterceptor
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.engine.gecko.ext.toContentBlockingSetting
import mozilla.components.browser.engine.gecko.permission.GeckoSitePermissionsStorage
import mozilla.components.browser.state.engine.EngineMiddleware
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.mediaquery.PreferredColorScheme
import mozilla.components.feature.app.links.AppLinksInterceptor
import mozilla.components.feature.prompts.PromptMiddleware
import mozilla.components.feature.prompts.file.FileUploadsDirCleaner
import mozilla.components.feature.session.SessionUseCases
import mozilla.components.feature.sitepermissions.OnDiskSitePermissionsStorage
import mozilla.components.feature.webcompat.WebCompatFeature
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

open class Components(private val applicationContext: Context) {

    fun darkEnabled(): PreferredColorScheme {
        val darkOn =
            (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                    Configuration.UI_MODE_NIGHT_YES
        return if (darkOn) PreferredColorScheme.Dark else PreferredColorScheme.Light
    }

    private val appRequestInterceptor by lazy {
        AppRequestInterceptor(applicationContext)
    }

    val appLinksInterceptor by lazy {
        AppLinksInterceptor(
            applicationContext,
            launchInApp = { BuildConfig.LAUNCH_IN_APP },
        )
    }

    private val engineSettings by lazy {
        DefaultSettings().apply {
            requestInterceptor = appRequestInterceptor
            remoteDebuggingEnabled = BuildConfig.REMOTE_DEBUGGING
            supportMultipleWindows = false
            enterpriseRootsEnabled = BuildConfig.TRUST_THIRD_PARTY_CERTS
            if (!BuildConfig.AUTO_FONT_SIZE) {
                fontSizeFactor = BuildConfig.FONT_SIZE_FACTOR
                automaticFontSizeAdjustment = false
            }
            preferredColorScheme = darkEnabled()
            javascriptEnabled = BuildConfig.JAVA_SCRIPT_ENABLED
        }
    }

    private val runtime by lazy {
        val runtimeSettings = GeckoRuntimeSettings.Builder()
            .debugLogging(BuildConfig.DEBUG)
            .contentBlocking(trackingPolicy.toContentBlockingSetting())
            .build()

        runtimeSettings.contentBlocking.setSafeBrowsing(safeBrowsingPolicy)

        if (BuildConfig.SAFE_BROWSING) {
            runtimeSettings.contentBlocking.setSafeBrowsingProviders(
                ContentBlocking.GOOGLE_SAFE_BROWSING_PROVIDER,
                ContentBlocking.GOOGLE_LEGACY_SAFE_BROWSING_PROVIDER,
            )
            runtimeSettings.contentBlocking.setSafeBrowsingMalwareTable(
                "goog-malware-proto",
                "goog-unwanted-proto",
            )
            runtimeSettings.contentBlocking.setSafeBrowsingPhishingTable("goog-phish-proto")
        } else {
            runtimeSettings.contentBlocking.setSafeBrowsingProviders()
            runtimeSettings.contentBlocking.setSafeBrowsingMalwareTable()
            runtimeSettings.contentBlocking.setSafeBrowsingPhishingTable()
        }

        GeckoRuntime.create(applicationContext, runtimeSettings)
    }

    open val engine: Engine by lazy {
        GeckoEngine(applicationContext, engineSettings, runtime).also {
            WebCompatFeature.install(it)
        }
    }

    val permissionStorage by lazy {
        GeckoSitePermissionsStorage(
            runtime,
            OnDiskSitePermissionsStorage(applicationContext),
        )
    }

    val store by lazy {
        BrowserStore(
            middleware = listOf(PromptMiddleware()) +
                EngineMiddleware.create(engine, trimMemoryAutomatically = false),
        )
    }

    val sessionUseCases by lazy { SessionUseCases(store) }

    val fileUploadsDirCleaner: FileUploadsDirCleaner by lazy {
        FileUploadsDirCleaner { applicationContext.cacheDir }
    }

    private val trackingPolicy by lazy {
        if (BuildConfig.TRACKING_PROTECTION) {
            EngineSession.TrackingProtectionPolicy.recommended()
        } else {
            EngineSession.TrackingProtectionPolicy.none()
        }
    }

    private val safeBrowsingPolicy by lazy {
        if (BuildConfig.SAFE_BROWSING) {
            ContentBlocking.SafeBrowsing.DEFAULT
        } else {
            ContentBlocking.SafeBrowsing.NONE
        }
    }
}
