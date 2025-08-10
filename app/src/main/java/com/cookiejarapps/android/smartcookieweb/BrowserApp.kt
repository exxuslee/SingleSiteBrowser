@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")
package com.cookiejarapps.android.smartcookieweb

import android.app.Application
import com.cookiejarapps.android.smartcookieweb.components.Components
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mozilla.components.browser.state.action.SystemAction
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.feature.addons.update.GlobalAddonDependencyProvider
import mozilla.components.support.base.facts.Facts
import mozilla.components.support.base.facts.processor.LogFactProcessor
import mozilla.components.support.base.log.logger.Logger
import mozilla.components.support.ktx.android.content.isMainProcess
import mozilla.components.support.ktx.android.content.runOnlyInMainProcess
import mozilla.components.support.locale.LocaleAwareApplication
import mozilla.components.support.webextensions.WebExtensionSupport
import java.util.concurrent.TimeUnit

class BrowserApp : Application() {

    private val logger = Logger("BrowserApp")

    val components by lazy { Components(this) }

    override fun onCreate() {
        super.onCreate()

        if (!isMainProcess()) {
            return
        }

        Facts.registerProcessor(LogFactProcessor())

        components.engine.warmUp()

        GlobalScope.launch(Dispatchers.IO) {
            components.webAppManifestStorage.warmUpScopes(System.currentTimeMillis())
        }
        components.downloadsUseCases.restoreDownloads()

        run {
            try {
                GlobalAddonDependencyProvider.initialize(
                    components.addonManager,
                    components.addonUpdater,
                    onCrash = { logger.error("Addon dependency provider crashed", it) },
                )
                WebExtensionSupport.initialize(
                    components.engine,
                    components.store,
                    onNewTabOverride = { _, engineSession, url ->
                        val shouldCreatePrivateSession =
                            components.store.state.selectedTab?.content?.private ?: false

                        components.tabsUseCases.addTab(
                            url = url,
                            selectTab = true,
                            engineSession = engineSession,
                            private = shouldCreatePrivateSession,
                        )
                    },
                    onCloseTabOverride = { _, sessionId ->
                        components.tabsUseCases.removeTab(sessionId)
                    },
                    onSelectTabOverride = { _, sessionId ->
                        components.tabsUseCases.selectTab(sessionId)
                    },
                    onExtensionsLoaded = { extensions ->
                        components.addonUpdater.registerForFutureUpdates(extensions)
                    },
                    onUpdatePermissionRequest = components.addonUpdater::onUpdatePermissionRequest,
                )
            } catch (e: UnsupportedOperationException) {
                Logger.error("Failed to initialize web extension support", e)
            }
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        runOnlyInMainProcess {
            components.icons.onTrimMemory(level)
            components.store.dispatch(SystemAction.LowMemoryAction(level))
        }
    }
}
