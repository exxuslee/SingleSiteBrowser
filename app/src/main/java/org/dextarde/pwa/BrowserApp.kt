package org.dextarde.pwa

import android.app.Application
import org.dextarde.pwa.components.Components
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mozilla.components.browser.state.action.SystemAction
import mozilla.components.support.base.facts.Facts
import mozilla.components.support.base.facts.processor.LogFactProcessor
import mozilla.components.support.base.log.logger.Logger
import mozilla.components.support.ktx.android.content.isMainProcess
import mozilla.components.support.ktx.android.content.runOnlyInMainProcess
import mozilla.components.support.webextensions.WebExtensionSupport

class BrowserApp : Application() {
    val components by lazy { Components(this) }

    override fun onCreate() {
        super.onCreate()

        if (!isMainProcess()) return
        Facts.registerProcessor(LogFactProcessor())
        components.engine.warmUp()

        GlobalScope.launch(Dispatchers.IO) {
            components.webAppManifestStorage.warmUpScopes(System.currentTimeMillis())
        }
        try {
            WebExtensionSupport.initialize(
                components.engine,
                components.store,
                onNewTabOverride = { _, engineSession, url ->
                    components.tabsUseCases.addTab(
                        url,
                        selectTab = true,
                        engineSession = engineSession
                    )
                },
                onCloseTabOverride = { _, sessionId ->
                    components.tabsUseCases.removeTab(sessionId)
                },
                onSelectTabOverride = { _, sessionId ->
                    components.tabsUseCases.selectTab(sessionId)
                },
            )
        } catch (e: UnsupportedOperationException) {
            Logger.error("Failed to initialize web extension support", e)
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
