package com.exzi.android;

import java.lang.System;

/**
 * Activity that holds the [BrowserFragment].
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00a4\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 O2\u00020\u00012\u00020\u0002:\u0001OB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0019H\u0014J\u0010\u0010\"\u001a\u00020\u000b2\u0006\u0010#\u001a\u00020$H\u0014J\u0012\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010\'\u001a\u00020(H\u0014J\b\u0010)\u001a\u00020 H\u0002J\u001f\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-2\b\u0010.\u001a\u0004\u0018\u00010/H\u0001\u00a2\u0006\u0002\b0J4\u00101\u001a\u00020 2\u0006\u00102\u001a\u00020&2\u0006\u00103\u001a\u00020+2\b\u00104\u001a\u0004\u0018\u0001052\u0006\u00106\u001a\u00020+2\b\b\u0002\u00107\u001a\u000208H\u0002J\"\u00109\u001a\u00020 2\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020;2\b\u0010=\u001a\u0004\u0018\u00010-H\u0004J\u0006\u0010>\u001a\u00020 J\u0012\u0010?\u001a\u00020 2\b\u0010@\u001a\u0004\u0018\u00010/H\u0014J,\u0010A\u001a\u0004\u0018\u00010B2\b\u0010C\u001a\u0004\u0018\u00010B2\u0006\u0010D\u001a\u00020&2\u0006\u0010E\u001a\u00020\u00192\u0006\u0010F\u001a\u00020GH\u0016J\u0010\u0010H\u001a\u00020 2\u0006\u0010I\u001a\u00020JH\u0002JJ\u0010K\u001a\u00020 2\u0006\u00102\u001a\u00020&2\u0006\u00103\u001a\u00020+2\u0006\u0010L\u001a\u00020M2\n\b\u0002\u0010N\u001a\u0004\u0018\u00010&2\n\b\u0002\u00104\u001a\u0004\u0018\u0001052\b\b\u0002\u00106\u001a\u00020+2\b\b\u0002\u00107\u001a\u000208R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001a\u001a\u00020\u001b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001e\u0010\u0017\u001a\u0004\b\u001c\u0010\u001d\u00a8\u0006P"}, d2 = {"Lcom/exzi/android/BrowserActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/content/ComponentCallbacks2;", "()V", "binding", "Lcom/exzi/android/databinding/ActivityMainBinding;", "getBinding", "()Lcom/exzi/android/databinding/ActivityMainBinding;", "setBinding", "(Lcom/exzi/android/databinding/ActivityMainBinding;)V", "browsingModeManager", "Lcom/exzi/android/browser/BrowsingModeManager;", "getBrowsingModeManager", "()Lcom/exzi/android/browser/BrowsingModeManager;", "setBrowsingModeManager", "(Lcom/exzi/android/browser/BrowsingModeManager;)V", "mPort", "Lmozilla/components/concept/engine/webextension/Port;", "navHost", "Landroidx/navigation/fragment/NavHostFragment;", "getNavHost", "()Landroidx/navigation/fragment/NavHostFragment;", "navHost$delegate", "Lkotlin/Lazy;", "originalContext", "Landroid/content/Context;", "webExtensionPopupFeature", "Lmozilla/components/support/webextensions/WebExtensionPopupFeature;", "getWebExtensionPopupFeature", "()Lmozilla/components/support/webextensions/WebExtensionPopupFeature;", "webExtensionPopupFeature$delegate", "attachBaseContext", "", "base", "createBrowsingModeManager", "initialMode", "Lcom/exzi/android/browser/BrowsingMode;", "getIntentSessionId", "", "intent", "Lmozilla/components/support/utils/SafeIntent;", "installPrintExtension", "isActivityColdStarted", "", "startingIntent", "Landroid/content/Intent;", "activityIcicle", "Landroid/os/Bundle;", "isActivityColdStarted$app_debug", "load", "searchTermOrURL", "newTab", "engine", "Lmozilla/components/browser/state/search/SearchEngine;", "forceSearch", "flags", "Lmozilla/components/concept/engine/EngineSession$LoadUrlFlags;", "onActivityResult", "requestCode", "", "resultCode", "data", "onBackPressed", "onCreate", "savedInstanceState", "onCreateView", "Landroid/view/View;", "parent", "name", "context", "attrs", "Landroid/util/AttributeSet;", "openPopup", "webExtensionState", "Lmozilla/components/browser/state/state/WebExtensionState;", "openToBrowserAndLoad", "from", "Lcom/exzi/android/BrowserDirection;", "customTabSessionId", "Companion", "app_debug"})
public class BrowserActivity extends androidx.appcompat.app.AppCompatActivity implements android.content.ComponentCallbacks2 {
    public com.exzi.android.databinding.ActivityMainBinding binding;
    public com.exzi.android.browser.BrowsingModeManager browsingModeManager;
    private final kotlin.Lazy navHost$delegate = null;
    private final kotlin.Lazy webExtensionPopupFeature$delegate = null;
    private mozilla.components.concept.engine.webextension.Port mPort;
    private android.content.Context originalContext;
    @org.jetbrains.annotations.NotNull()
    public static final com.exzi.android.BrowserActivity.Companion Companion = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String OPEN_TO_BROWSER = "open_to_browser";
    
    public BrowserActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.exzi.android.databinding.ActivityMainBinding getBinding() {
        return null;
    }
    
    public final void setBinding(@org.jetbrains.annotations.NotNull()
    com.exzi.android.databinding.ActivityMainBinding p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.exzi.android.browser.BrowsingModeManager getBrowsingModeManager() {
        return null;
    }
    
    public final void setBrowsingModeManager(@org.jetbrains.annotations.NotNull()
    com.exzi.android.browser.BrowsingModeManager p0) {
    }
    
    private final androidx.navigation.fragment.NavHostFragment getNavHost() {
        return null;
    }
    
    private final mozilla.components.support.webextensions.WebExtensionPopupFeature getWebExtensionPopupFeature() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected java.lang.String getIntentSessionId(@org.jetbrains.annotations.NotNull()
    mozilla.components.support.utils.SafeIntent intent) {
        return null;
    }
    
    @androidx.annotation.VisibleForTesting()
    public final boolean isActivityColdStarted$app_debug(@org.jetbrains.annotations.NotNull()
    android.content.Intent startingIntent, @org.jetbrains.annotations.Nullable()
    android.os.Bundle activityIcicle) {
        return false;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected com.exzi.android.browser.BrowsingModeManager createBrowsingModeManager(@org.jetbrains.annotations.NotNull()
    com.exzi.android.browser.BrowsingMode initialMode) {
        return null;
    }
    
    @java.lang.Override()
    public final void onBackPressed() {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.Nullable()
    android.view.View parent, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.util.AttributeSet attrs) {
        return null;
    }
    
    @java.lang.Override()
    protected final void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void openPopup(mozilla.components.browser.state.state.WebExtensionState webExtensionState) {
    }
    
    @kotlin.Suppress(names = {"LongParameterList"})
    public final void openToBrowserAndLoad(@org.jetbrains.annotations.NotNull()
    java.lang.String searchTermOrURL, boolean newTab, @org.jetbrains.annotations.NotNull()
    com.exzi.android.BrowserDirection from, @org.jetbrains.annotations.Nullable()
    java.lang.String customTabSessionId, @org.jetbrains.annotations.Nullable()
    mozilla.components.browser.state.search.SearchEngine engine, boolean forceSearch, @org.jetbrains.annotations.NotNull()
    mozilla.components.concept.engine.EngineSession.LoadUrlFlags flags) {
    }
    
    private final void load(java.lang.String searchTermOrURL, boolean newTab, mozilla.components.browser.state.search.SearchEngine engine, boolean forceSearch, mozilla.components.concept.engine.EngineSession.LoadUrlFlags flags) {
    }
    
    private final void installPrintExtension() {
    }
    
    @java.lang.Override()
    protected void attachBaseContext(@org.jetbrains.annotations.NotNull()
    android.content.Context base) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/exzi/android/BrowserActivity$Companion;", "", "()V", "OPEN_TO_BROWSER", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}