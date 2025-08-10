package com.cookiejarapps.android.smartcookieweb.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import mozilla.components.support.ktx.android.content.*

class UserPreferences(appContext: Context): PreferencesHolder {

    override val preferences: SharedPreferences =
        appContext.getSharedPreferences(SCW_PREFERENCES, MODE_PRIVATE)

    // Preferences
    var javaScriptEnabled by booleanPreference(JAVA_SCRIPT_ENABLED, true)
    var customSearchEngine by booleanPreference(CUSTOM_SEARCH_ENGINE, false)
    var customSearchEngineURL by stringPreference(CUSTOM_SEARCH_ENGINE_URL, "")
    var customHomepageUrl by stringPreference(HOMEPAGE_URL, "")
    var launchInApp by booleanPreference(LAUNCH_IN_APP, true)
    var customAddonCollection by booleanPreference(CUSTOM_ADDON_BOOL, false)
    var customAddonCollectionUser by stringPreference(COLLECTION_USER, "")
    var customAddonCollectionName by stringPreference(COLLECTION_NAME, "")
    var autoFontSize by booleanPreference(AUTO_FONT_SIZE, true)
    var fontSizeFactor by floatPreference(FONT_SIZE_FACTOR, 1f)
    var hideBarWhileScrolling by booleanPreference(HIDE_URL_BAR, true)
     var swipeToRefresh by booleanPreference(SWIPE_TO_REFRESH, true)
    var remoteDebugging by booleanPreference(REMOTE_DEBUGGING, false)
     var safeBrowsing by booleanPreference(SAFE_BROWSING, true)
    var trackingProtection by booleanPreference(TRACKING_PROTECTION, true)
    var trustThirdPartyCerts by booleanPreference(TRUST_THIRD_PARTY_CERTS, false)

    companion object {
        const val SCW_PREFERENCES = "scw_preferences"

        const val JAVA_SCRIPT_ENABLED = "java_script_enabled"
        const val SHOW_ADDONS_IN_BAR = "show_addons_in_bar"
        const val SEARCH_ENGINE = "search_engine"
        const val CUSTOM_SEARCH_ENGINE = "custom_search_engine"
        const val CUSTOM_SEARCH_ENGINE_URL = "custom_search_engine_url"
        const val TOOLBAR_POSITION = "toolbar_position"
        const val HOMEPAGE_TYPE = "homepage_type"
        const val HOMEPAGE_URL = "homepage_url"
        const val APP_THEME_CHOICE = "app_theme_choice"
        const val WEB_THEME_CHOICE = "web_theme_choice"
        const val LAUNCH_IN_APP = "launch_in_app"
        const val CUSTOM_ADDON_BOOL = "custom_addon_bool"
        const val SHOWN_ADDON_DISCLAIMER = "shown_disclaimer"
        const val COLLECTION_NAME = "collection_name"
        const val COLLECTION_USER = "collection_user"
        const val AUTO_FONT_SIZE = "auto_font_size"
        const val FONT_SIZE_FACTOR = "font_size_factor"
        const val HIDE_URL_BAR = "hide_url_bar"
        const val SWAP_DRAWERS = "swap_drawers"
        const val STACK_FROM_BOTTOM = "stack_from_bottom"
        const val SHOW_TABS_IN_GRID = "show_tabs_in_grid"
        const val SWIPE_TO_REFRESH = "swipe_to_refresh"
        const val REMOTE_DEBUGGING = "remote_debugging"
        const val PROMPT_EXTERNAL_DOWNLOADER = "prompt_external_downloader"
        const val SHOW_URL_PROTOCOL = "show_url_protocol"
        const val SEARCH_SUGGESTIONS = "search_suggestions"
        const val SAFE_BROWSING = "safe_browsing"
        const val TRACKING_PROTECTION = "tracking_protection"
        const val SHOW_SHORTCUTS = "show_shortcuts"
        const val TRUST_THIRD_PARTY_CERTS = "trust_third_party_certs"
        const val HOMEPAGE_BACKGROUND_CHOICE = "homepage_background_choice"
        const val HOMEPAGE_BACKGROUND_URL = "homepage_background_url"
        const val LOAD_SHORTCUT_ICONS = "load_shortcut_icons"
        const val BAR_ADDONS_LIST = "bar_addons_list"
        const val BOOKMARK_SORT_TYPE = "bookmark_sort_type"
    }
}