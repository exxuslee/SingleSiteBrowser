package com.cookiejarapps.android.smartcookieweb.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.cookiejarapps.android.smartcookieweb.browser.HomepageChoice
import com.cookiejarapps.android.smartcookieweb.browser.ThemeChoice
import mozilla.components.support.ktx.android.content.*

class UserPreferences(appContext: Context): PreferencesHolder {

    override val preferences: SharedPreferences =
        appContext.getSharedPreferences(SCW_PREFERENCES, MODE_PRIVATE)

    // Saved values
    var lastKnownPrivate by booleanPreference("last_known_mode_private", false)
    var firstLaunch by booleanPreference("first_launch", true)

    // Preferences
    var javaScriptEnabled by booleanPreference(JAVA_SCRIPT_ENABLED, true)
    var searchEngineChoice by intPreference(SEARCH_ENGINE, 0)
    var customSearchEngine by booleanPreference(CUSTOM_SEARCH_ENGINE, false)
    var customSearchEngineURL by stringPreference(CUSTOM_SEARCH_ENGINE_URL, "")
    var homepageType by intPreference(HOMEPAGE_TYPE, HomepageChoice.VIEW.ordinal)
    var customHomepageUrl by stringPreference(HOMEPAGE_URL, "")
    var appThemeChoice by intPreference(APP_THEME_CHOICE, ThemeChoice.SYSTEM.ordinal)
    var webThemeChoice by intPreference(WEB_THEME_CHOICE, ThemeChoice.SYSTEM.ordinal)
    var launchInApp by booleanPreference(LAUNCH_IN_APP, true)
    var autoFontSize by booleanPreference(AUTO_FONT_SIZE, true)
    var fontSizeFactor by floatPreference(FONT_SIZE_FACTOR, 1f)
    var hideBarWhileScrolling by booleanPreference(HIDE_URL_BAR, true)
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
        const val SEARCH_ENGINE = "search_engine"
        const val CUSTOM_SEARCH_ENGINE = "custom_search_engine"
        const val CUSTOM_SEARCH_ENGINE_URL = "custom_search_engine_url"
        const val HOMEPAGE_TYPE = "homepage_type"
        const val HOMEPAGE_URL = "homepage_url"
        const val APP_THEME_CHOICE = "app_theme_choice"
        const val WEB_THEME_CHOICE = "web_theme_choice"
        const val LAUNCH_IN_APP = "launch_in_app"
        const val AUTO_FONT_SIZE = "auto_font_size"
        const val FONT_SIZE_FACTOR = "font_size_factor"
        const val HIDE_URL_BAR = "hide_url_bar"
        const val STACK_FROM_BOTTOM = "stack_from_bottom"
        const val SHOW_TABS_IN_GRID = "show_tabs_in_grid"
        const val SWIPE_TO_REFRESH = "swipe_to_refresh"
        const val REMOTE_DEBUGGING = "remote_debugging"
        const val SAFE_BROWSING = "safe_browsing"
        const val TRACKING_PROTECTION = "tracking_protection"
        const val TRUST_THIRD_PARTY_CERTS = "trust_third_party_certs"
    }
}