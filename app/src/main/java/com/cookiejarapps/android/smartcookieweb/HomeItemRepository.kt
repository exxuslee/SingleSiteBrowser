package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import org.json.JSONArray

object HomeItemRepository {

    fun loadItems(context: Context): List<HomeItem> {
        val json = context.assets.open("sites.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        return buildList {
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val title = item.getString("title")
                val icon = item.getString("icon")
                when {
                    item.has("package") -> add(
                        HomeItem.ExternalApp(
                            title = title,
                            icon = icon,
                            packageName = item.getString("package"),
                            description = item.optString("description", ""),
                        ),
                    )
                    item.has("url") -> add(
                        HomeItem.Website(
                            title = title,
                            icon = icon,
                            url = item.getString("url"),
                            description = item.optString("description", ""),
                        ),
                    )
                }
            }
        }
    }
}
