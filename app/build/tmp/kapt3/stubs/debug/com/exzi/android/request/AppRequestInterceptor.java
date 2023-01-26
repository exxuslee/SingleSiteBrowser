package com.exzi.android.request;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000  2\u00020\u0001:\u0002 !B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J$\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\f\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016JL\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0017\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0019H\u0016J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0007\u001a\u00020\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/exzi/android/request/AppRequestInterceptor;", "Lmozilla/components/concept/engine/request/RequestInterceptor;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "navController", "Ljava/lang/ref/WeakReference;", "Landroidx/navigation/NavController;", "getErrorCategory", "Lcom/exzi/android/request/AppRequestInterceptor$ErrorCategory;", "errorType", "Lmozilla/components/browser/errorpages/ErrorType;", "onErrorRequest", "Lmozilla/components/concept/engine/request/RequestInterceptor$ErrorResponse;", "session", "Lmozilla/components/concept/engine/EngineSession;", "uri", "", "onLoadRequest", "Lmozilla/components/concept/engine/request/RequestInterceptor$InterceptionResponse;", "engineSession", "lastUri", "hasUserGesture", "", "isSameDomain", "isRedirect", "isDirectNavigation", "isSubframeRequest", "setNavController", "", "Companion", "ErrorCategory", "app_debug"})
public final class AppRequestInterceptor implements mozilla.components.concept.engine.request.RequestInterceptor {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private java.lang.ref.WeakReference<androidx.navigation.NavController> navController;
    @org.jetbrains.annotations.NotNull()
    public static final com.exzi.android.request.AppRequestInterceptor.Companion Companion = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NETWORK_ERROR_PAGE = "network_error_page.html";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SSL_ERROR_PAGE = "ssl_error_page.html";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MALWARE_ERROR_PAGE = "malware_error_page.html";
    
    public AppRequestInterceptor(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getContext() {
        return null;
    }
    
    public final void setNavController(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public mozilla.components.concept.engine.request.RequestInterceptor.InterceptionResponse onLoadRequest(@org.jetbrains.annotations.NotNull()
    mozilla.components.concept.engine.EngineSession engineSession, @org.jetbrains.annotations.NotNull()
    java.lang.String uri, @org.jetbrains.annotations.Nullable()
    java.lang.String lastUri, boolean hasUserGesture, boolean isSameDomain, boolean isRedirect, boolean isDirectNavigation, boolean isSubframeRequest) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public mozilla.components.concept.engine.request.RequestInterceptor.ErrorResponse onErrorRequest(@org.jetbrains.annotations.NotNull()
    mozilla.components.concept.engine.EngineSession session, @org.jetbrains.annotations.NotNull()
    mozilla.components.browser.errorpages.ErrorType errorType, @org.jetbrains.annotations.Nullable()
    java.lang.String uri) {
        return null;
    }
    
    private final com.exzi.android.request.AppRequestInterceptor.ErrorCategory getErrorCategory(mozilla.components.browser.errorpages.ErrorType errorType) {
        return null;
    }
    
    public boolean interceptsAppInitiatedRequests() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0080\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2 = {"Lcom/exzi/android/request/AppRequestInterceptor$ErrorCategory;", "", "htmlRes", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getHtmlRes", "()Ljava/lang/String;", "Network", "SSL", "Malware", "app_debug"})
    public static enum ErrorCategory {
        /*public static final*/ Network /* = new Network(null) */,
        /*public static final*/ SSL /* = new SSL(null) */,
        /*public static final*/ Malware /* = new Malware(null) */;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String htmlRes = null;
        
        ErrorCategory(java.lang.String htmlRes) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getHtmlRes() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0080T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/exzi/android/request/AppRequestInterceptor$Companion;", "", "()V", "MALWARE_ERROR_PAGE", "", "NETWORK_ERROR_PAGE", "SSL_ERROR_PAGE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}