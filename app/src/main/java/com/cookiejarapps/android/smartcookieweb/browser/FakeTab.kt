package com.cookiejarapps.android.smartcookieweb.browser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.doOnNextLayout
import com.cookiejarapps.android.smartcookieweb.R
import com.cookiejarapps.android.smartcookieweb.databinding.TabPreviewBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import mozilla.components.browser.thumbnails.loader.ThumbnailLoader
import mozilla.components.concept.base.images.ImageLoadRequest
import kotlin.math.max

class FakeTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = TabPreviewBinding.inflate(LayoutInflater.from(context), this)
    private val thumbnailLoader = ThumbnailLoader(context.components.thumbnailStorage)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.tab_preview, this, true)
    }



    fun loadPreviewThumbnail(thumbnailId: String) {
        doOnNextLayout {
            val thumbnailSize = max(binding.previewThumbnail.height, binding.previewThumbnail.width)
            thumbnailLoader.loadIntoView(
                binding.previewThumbnail,
                ImageLoadRequest(thumbnailId, thumbnailSize)
            )
        }
    }
}
