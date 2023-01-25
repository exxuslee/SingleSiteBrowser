package com.cookiejarapps.android.smartcookieweb.integration

import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.view.isVisible
import mozilla.components.browser.state.selector.findCustomTabOrSelectedTab
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.browser.toolbar.BrowserToolbar
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.findinpage.FindInPageFeature
import mozilla.components.feature.findinpage.view.FindInPageView
import mozilla.components.support.base.feature.LifecycleAwareFeature

@Suppress("UndocumentedPublicClass")
class FindInPageIntegration(
    private val store: BrowserStore,
    private val sessionId: String? = null,
    stub: ViewStub,
    private val engineView: EngineView,
) : InflationAwareFeature(stub) {
    override fun onViewInflated(view: View): LifecycleAwareFeature {
        return FindInPageFeature(store, view as FindInPageView, engineView) {
            view.visibility = View.GONE
        }
    }

    override fun onLaunch(view: View, feature: LifecycleAwareFeature) {
        store.state.findCustomTabOrSelectedTab(sessionId)?.let { tab ->
            view.visibility = View.VISIBLE
            (feature as FindInPageFeature).bind(tab)
        }
    }
}