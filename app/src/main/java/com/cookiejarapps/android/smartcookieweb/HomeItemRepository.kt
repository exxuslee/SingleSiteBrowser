package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

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
                            packageName = item.getStringList("package"),
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

    private fun JSONObject.getStringList(key: String): List<String> =
        when (val raw = get(key)) {
            is JSONArray -> List(raw.length()) { raw.getString(it) }
            else -> listOf(raw.toString())
        }
}
