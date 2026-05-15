package com.cookiejarapps.android.smartcookieweb

import android.os.Bundle
import android.view.View
import com.cookiejarapps.android.smartcookieweb.ext.components
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.SessionState

@ExperimentalCoroutinesApi
class BrowserFragment : BaseBrowserFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val components = requireContext().components
        if (components.store.state.selectedTab == null) {
            components.tabsUseCases.addTab(BuildConfig.SITE_URL, selectTab = true)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeUI(view: View, tab: SessionState) {
        super.initializeUI(view, tab)
        val currentUrl = tab.content.url
        if (currentUrl.isBlank() || currentUrl == "about:blank") {
            requireContext().components.sessionUseCases.loadUrl(BuildConfig.SITE_URL)
        }
    }
}
