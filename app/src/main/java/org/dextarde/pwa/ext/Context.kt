package org.dextarde.pwa.ext

import android.content.Context
import org.dextarde.pwa.BrowserApp
import org.dextarde.pwa.components.Components

// get app from context
val Context.application: BrowserApp
    get() = applicationContext as BrowserApp

// get components from context
val Context.components: Components
    get() = application.components
