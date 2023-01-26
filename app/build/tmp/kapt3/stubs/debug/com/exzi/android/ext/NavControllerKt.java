package com.exzi.android.ext;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 2, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\n\b\u0001\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u0005\u001a/\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\n\b\u0001\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\r\u001a\u001c\u0010\u000e\u001a\u00020\u0007*\u00020\u00022\b\b\u0001\u0010\u000f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n\u00a8\u0006\u0010"}, d2 = {"alreadyOnDestination", "", "Landroidx/navigation/NavController;", "destId", "", "(Landroidx/navigation/NavController;Ljava/lang/Integer;)Z", "nav", "", "id", "directions", "Landroidx/navigation/NavDirections;", "navOptions", "Landroidx/navigation/NavOptions;", "(Landroidx/navigation/NavController;Ljava/lang/Integer;Landroidx/navigation/NavDirections;Landroidx/navigation/NavOptions;)V", "navigateSafe", "resId", "app_debug"})
public final class NavControllerKt {
    
    /**
     * Navigate from the fragment with [id] using the given [directions].
     * If the id doesn't match the current destination, an error is recorded.
     */
    public static final void nav(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController $this$nav, @org.jetbrains.annotations.Nullable()
    @androidx.annotation.IdRes()
    java.lang.Integer id, @org.jetbrains.annotations.NotNull()
    androidx.navigation.NavDirections directions, @org.jetbrains.annotations.Nullable()
    androidx.navigation.NavOptions navOptions) {
    }
    
    public static final boolean alreadyOnDestination(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController $this$alreadyOnDestination, @org.jetbrains.annotations.Nullable()
    @androidx.annotation.IdRes()
    java.lang.Integer destId) {
        return false;
    }
    
    public static final void navigateSafe(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController $this$navigateSafe, @androidx.annotation.IdRes()
    int resId, @org.jetbrains.annotations.NotNull()
    androidx.navigation.NavDirections directions) {
    }
}