package com.cookiejarapps.android.smartcookieweb.ext

import android.content.Context
import android.content.res.Configuration
import com.cookiejarapps.android.smartcookieweb.BrowserApp
import com.cookiejarapps.android.smartcookieweb.components.Components
import com.cookiejarapps.android.smartcookieweb.preferences.UserPreferences

// get app from context
val Context.application: BrowserApp
    get() = applicationContext as BrowserApp

// get components from context
val Context.components: Components
    get() = application.components