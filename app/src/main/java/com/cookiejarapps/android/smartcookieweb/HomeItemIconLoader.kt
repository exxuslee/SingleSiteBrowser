package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import java.io.IOException

object HomeItemIconLoader {

    fun load(context: Context, icon: String): Drawable? {
        loadFromDrawable(context, icon)?.let { return it }

        val assetPath = when {
            icon.startsWith("assets/") -> icon.removePrefix("assets/")
            else -> icon
        }
        return loadFromAssets(context, assetPath)
    }

    private fun loadFromDrawable(context: Context, icon: String): Drawable? {
        val name = when {
            icon.startsWith("@drawable/") -> icon.removePrefix("@drawable/")
            '/' in icon || icon.contains('.') -> return null
            else -> icon
        }
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        if (resId == 0) return null
        return AppCompatResources.getDrawable(context, resId)
    }

    private fun loadFromAssets(context: Context, path: String): Drawable? =
        try {
            context.assets.open(path).use { stream ->
                Drawable.createFromStream(stream, null)
            }
        } catch (_: IOException) {
            null
        }
}
