package com.exzi.android.browser;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/exzi/android/browser/BrowsingModeManager;", "", "mode", "Lcom/exzi/android/browser/BrowsingMode;", "getMode", "()Lcom/exzi/android/browser/BrowsingMode;", "setMode", "(Lcom/exzi/android/browser/BrowsingMode;)V", "app_debug"})
public abstract interface BrowsingModeManager {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.exzi.android.browser.BrowsingMode getMode();
    
    public abstract void setMode(@org.jetbrains.annotations.NotNull()
    com.exzi.android.browser.BrowsingMode p0);
}