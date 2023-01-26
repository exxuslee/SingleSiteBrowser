package com.exzi.android.ext

import android.content.Context
import com.exzi.android.BrowserApp
import com.exzi.android.components.Components

// get app from context
val Context.application: BrowserApp
    get() = applicationContext as BrowserApp

// get components from context
val Context.components: Components
    get() = application.components
