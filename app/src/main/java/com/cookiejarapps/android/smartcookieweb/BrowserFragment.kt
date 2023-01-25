package com.cookiejarapps.android.smartcookieweb

import android.os.Bundle
import android.view.View
import com.cookiejarapps.android.smartcookieweb.ext.components
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mozilla.components.browser.state.state.SessionState
import mozilla.components.browser.thumbnails.BrowserThumbnails
import mozilla.components.feature.tabs.WindowFeature
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.base.feature.ViewBoundFeatureWrapper

/**
 * Fragment used for browsing the web within the main app.
 */
@ExperimentalCoroutinesApi
@Suppress("TooManyFunctions", "LargeClass")
class BrowserFragment : BaseBrowserFragment(), UserInteractionHandler {

    private val windowFeature = ViewBoundFeatureWrapper<WindowFeature>()

    @Suppress("LongMethod")
    override fun initializeUI(view: View, tab: SessionState) {
        super.initializeUI(view, tab)

        val context = requireContext()
        val components = context.components

        thumbnailsFeature.set(
            feature = BrowserThumbnails(context, binding.engineView, components.store),
            owner = this,
            view = view
        )

        windowFeature.set(
            feature = WindowFeature(
                store = components.store,
                tabsUseCases = components.tabsUseCases
            ),
            owner = this,
            view = view
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as BrowserActivity)
            .openToBrowserAndLoad(searchTermOrURL =  "exzi.com",
                newTab = false, from = BrowserDirection.FromGlobal)
    }
}