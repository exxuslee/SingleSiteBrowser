package com.exzi.android.browser;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0001\u0018\u0000 \b2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\t"}, d2 = {"Lcom/exzi/android/browser/BrowsingMode;", "", "(Ljava/lang/String;I)V", "isPrivate", "", "()Z", "Normal", "Private", "Companion", "app_debug"})
public enum BrowsingMode {
    /*public static final*/ Normal /* = new Normal() */,
    /*public static final*/ Private /* = new Private() */;
    @org.jetbrains.annotations.NotNull()
    public static final com.exzi.android.browser.BrowsingMode.Companion Companion = null;
    
    BrowsingMode() {
    }
    
    public final boolean isPrivate() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/exzi/android/browser/BrowsingMode$Companion;", "", "()V", "fromBoolean", "Lcom/exzi/android/browser/BrowsingMode;", "isPrivate", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.exzi.android.browser.BrowsingMode fromBoolean(boolean isPrivate) {
            return null;
        }
    }
}