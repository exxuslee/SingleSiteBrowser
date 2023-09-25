package org.dextarde.pwa.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import mozilla.components.support.ktx.android.content.*

class UserPreferences(appContext: Context): PreferencesHolder {

    override val preferences: SharedPreferences =
        appContext.getSharedPreferences(SCW_PREFERENCES, MODE_PRIVATE)

    // Saved values
    var lastKnownPrivate by booleanPreference("last_known_mode_private", false)
    var firstLaunch by booleanPreference("first_launch", true)

    // Preferences
    var javaScriptEnabled by booleanPreference(JAVA_SCRIPT_ENABLED, true)
    var customHomepageUrl by stringPreference(HOMEPAGE_URL, "")
    var launchInApp by booleanPreference(LAUNCH_IN_APP, true)
    var autoFontSize by booleanPreference(AUTO_FONT_SIZE, true)
    var fontSizeFactor by floatPreference(FONT_SIZE_FACTOR, 1f)
    var stackFromBottom by booleanPreference(STACK_FROM_BOTTOM, false)
    var showTabsInGrid by booleanPreference(SHOW_TABS_IN_GRID, false)
    var swipeToRefresh by booleanPreference(SWIPE_TO_REFRESH, true)
    var remoteDebugging by booleanPreference(REMOTE_DEBUGGING, false)
    var safeBrowsing by booleanPreference(SAFE_BROWSING, true)
    var trackingProtection by booleanPreference(TRACKING_PROTECTION, true)
    var trustThirdPartyCerts by booleanPreference(TRUST_THIRD_PARTY_CERTS, false)

    companion object {
        const val SCW_PREFERENCES = "scw_preferences"

        const val JAVA_SCRIPT_ENABLED = "java_script_enabled"
        const val HOMEPAGE_TYPE = "homepage_type"
        const val HOMEPAGE_URL = "homepage_url"
        const val LAUNCH_IN_APP = "launch_in_app"
        const val AUTO_FONT_SIZE = "auto_font_size"
        const val FONT_SIZE_FACTOR = "font_size_factor"
        const val STACK_FROM_BOTTOM = "stack_from_bottom"
        const val SHOW_TABS_IN_GRID = "show_tabs_in_grid"
        const val SWIPE_TO_REFRESH = "swipe_to_refresh"
        const val REMOTE_DEBUGGING = "remote_debugging"
        const val SAFE_BROWSING = "safe_browsing"
        const val TRACKING_PROTECTION = "tracking_protection"
        const val TRUST_THIRD_PARTY_CERTS = "trust_third_party_certs"
    }
}