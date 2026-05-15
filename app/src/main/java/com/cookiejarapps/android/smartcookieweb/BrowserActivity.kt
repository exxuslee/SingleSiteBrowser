package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cookiejarapps.android.smartcookieweb.databinding.ActivityMainBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import mozilla.components.concept.engine.EngineSession
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.contextmenu.ext.DefaultSelectionActionDelegate
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.UserInteractionHandler
import mozilla.components.support.ktx.kotlin.toNormalizedUrl

open class BrowserActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach {
            if (it is UserInteractionHandler && it.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet,
    ): View? =
        when (name) {
            EngineView::class.java.name -> components.engine.createView(context, attrs).apply {
                selectionActionDelegate = DefaultSelectionActionDelegate(
                    store = components.store,
                    context = context,
                )
            }.asView()

            else -> super.onCreateView(parent, name, context, attrs)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach {
            if (it is ActivityResultHandler && it.onActivityResult(requestCode, data, resultCode)) {
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun load(
        searchTermOrURL: String,
        flags: EngineSession.LoadUrlFlags = EngineSession.LoadUrlFlags.none(),
    ) {
        components.sessionUseCases.loadUrl.invoke(searchTermOrURL.toNormalizedUrl(), flags)
    }
}
