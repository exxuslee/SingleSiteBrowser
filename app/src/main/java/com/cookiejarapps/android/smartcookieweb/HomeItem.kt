package com.cookiejarapps.android.smartcookieweb

sealed class HomeItem {
    abstract val title: String
    abstract val icon: String
    abstract val description: String

    data class Website(
        override val title: String,
        override val icon: String,
        val url: String,
        override val description: String,
    ) : HomeItem()

    data class ExternalApp(
        override val title: String,
        override val icon: String,
        val packageName: String,
        override val description: String,
    ) : HomeItem()
}
