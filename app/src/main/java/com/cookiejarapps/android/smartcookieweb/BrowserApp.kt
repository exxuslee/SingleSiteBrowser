package com.cookiejarapps.android.smartcookieweb

import android.app.Application
import com.cookiejarapps.android.smartcookieweb.components.Components
import mozilla.components.browser.state.action.SystemAction
import mozilla.components.support.ktx.android.content.isMainProcess
import mozilla.components.support.ktx.android.content.runOnlyInMainProcess

class BrowserApp : Application() {

    val components by lazy { Components(this) }

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess()) {
            return
        }
        components.engine.warmUp()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        runOnlyInMainProcess {
            components.store.dispatch(SystemAction.LowMemoryAction(level))
        }
    }
}
