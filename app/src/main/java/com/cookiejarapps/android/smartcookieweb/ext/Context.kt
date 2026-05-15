package com.cookiejarapps.android.smartcookieweb.ext

import android.content.Context
import com.cookiejarapps.android.smartcookieweb.BrowserApp
import com.cookiejarapps.android.smartcookieweb.components.Components

val Context.components: Components
    get() = (applicationContext as BrowserApp).components
