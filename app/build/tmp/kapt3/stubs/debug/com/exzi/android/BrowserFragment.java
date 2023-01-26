package com.exzi.android;

import java.lang.System;

/**
 * Fragment used for browsing the web within the main app.
 */
@kotlin.Suppress(names = {"TooManyFunctions", "LargeClass"})
@kotlinx.coroutines.ExperimentalCoroutinesApi()
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001d\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0010\u00a2\u0006\u0002\b\rJ$\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/exzi/android/BrowserFragment;", "Lcom/exzi/android/BaseBrowserFragment;", "Lmozilla/components/support/base/feature/UserInteractionHandler;", "()V", "windowFeature", "Lmozilla/components/support/base/feature/ViewBoundFeatureWrapper;", "Lmozilla/components/feature/tabs/WindowFeature;", "initializeUI", "", "view", "Landroid/view/View;", "tab", "Lmozilla/components/browser/state/state/SessionState;", "initializeUI$app_debug", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class BrowserFragment extends com.exzi.android.BaseBrowserFragment implements mozilla.components.support.base.feature.UserInteractionHandler {
    private final mozilla.components.support.base.feature.ViewBoundFeatureWrapper<mozilla.components.feature.tabs.WindowFeature> windowFeature = null;
    
    public BrowserFragment() {
        super();
    }
    
    @kotlin.Suppress(names = {"LongMethod"})
    @java.lang.Override()
    public void initializeUI$app_debug(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.NotNull()
    mozilla.components.browser.state.state.SessionState tab) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
}