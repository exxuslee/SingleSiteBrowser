package com.exzi.android.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u00062\b\u0010\u000f\u001a\u0004\u0018\u00010\b2\b\u0010\u0010\u001a\u0004\u0018\u00010\bJ\b\u0010\u0011\u001a\u00020\rH\u0002J\b\u0010\u0012\u001a\u00020\rH\u0016J\u0010\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u0001H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/exzi/android/utils/PrintUtils;", "Ljava/lang/Runnable;", "()V", "mAlreadyRunning", "", "mContext", "Landroid/content/Context;", "mHtmlString", "", "mUrl", "mWebView", "Landroid/webkit/WebView;", "convert", "", "context", "htmlString", "url", "destroy", "run", "runOnUiThread", "runnable", "Companion", "app_debug"})
public final class PrintUtils implements java.lang.Runnable {
    private android.content.Context mContext;
    private java.lang.String mHtmlString;
    private java.lang.String mUrl;
    private boolean mAlreadyRunning = false;
    private android.webkit.WebView mWebView;
    @org.jetbrains.annotations.NotNull()
    public static final com.exzi.android.utils.PrintUtils.Companion Companion = null;
    @android.annotation.SuppressLint(value = {"StaticFieldLeak"})
    private static com.exzi.android.utils.PrintUtils printInstance;
    
    private PrintUtils() {
        super();
    }
    
    @java.lang.Override()
    public void run() {
    }
    
    public final void convert(@org.jetbrains.annotations.Nullable()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String htmlString, @org.jetbrains.annotations.Nullable()
    java.lang.String url) {
    }
    
    private final void runOnUiThread(java.lang.Runnable runnable) {
    }
    
    private final void destroy() {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00048\u0002@\u0002X\u0083\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/exzi/android/utils/PrintUtils$Companion;", "", "()V", "instance", "Lcom/exzi/android/utils/PrintUtils;", "getInstance", "()Lcom/exzi/android/utils/PrintUtils;", "printInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        @kotlin.jvm.Synchronized()
        public final synchronized com.exzi.android.utils.PrintUtils getInstance() {
            return null;
        }
    }
}