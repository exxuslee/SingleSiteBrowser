package com.cookiejarapps.android.smartcookieweb.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import mozilla.components.support.ktx.android.content.*

class UserPreferences(appContext: Context) : PreferencesHolder {

    override val preferences: SharedPreferences =
        appContext.getSharedPreferences(SCW_PREFERENCES, MODE_PRIVATE)

    var javaScriptEnabled by booleanPreference(JAVA_SCRIPT_ENABLED, true)
    var launchInApp by booleanPreference(LAUNCH_IN_APP, true)
    var autoFontSize by booleanPreference(AUTO_FONT_SIZE, true)
    var fontSizeFactor by floatPreference(FONT_SIZE_FACTOR, 1f)
    var swipeToRefresh by booleanPreference(SWIPE_TO_REFRESH, true)
    var remoteDebugging by booleanPreference(REMOTE_DEBUGGING, false)
    var safeBrowsing by booleanPreference(SAFE_BROWSING, true)
    var trackingProtection by booleanPreference(TRACKING_PROTECTION, true)
    var trustThirdPartyCerts by booleanPreference(TRUST_THIRD_PARTY_CERTS, false)

    companion object {
        const val SCW_PREFERENCES = "scw_preferences"

        private const val JAVA_SCRIPT_ENABLED = "java_script_enabled"
        private const val LAUNCH_IN_APP = "launch_in_app"
        private const val AUTO_FONT_SIZE = "auto_font_size"
        private const val FONT_SIZE_FACTOR = "font_size_factor"
        private const val SWIPE_TO_REFRESH = "swipe_to_refresh"
        private const val REMOTE_DEBUGGING = "remote_debugging"
        private const val SAFE_BROWSING = "safe_browsing"
        private const val TRACKING_PROTECTION = "tracking_protection"
        private const val TRUST_THIRD_PARTY_CERTS = "trust_third_party_certs"
    }
}
