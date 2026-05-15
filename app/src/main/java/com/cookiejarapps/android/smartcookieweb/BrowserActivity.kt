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
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.contextmenu.ext.DefaultSelectionActionDelegate
import mozilla.components.support.base.feature.ActivityResultHandler
import mozilla.components.support.base.feature.UserInteractionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val browserFragment: BrowserFragment?
        get() = supportFragmentManager.findFragmentById(R.id.browser_fragment) as? BrowserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (browserFragment?.onBackPressed() == true) {
            return
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
        if (browserFragment?.onActivityResult(requestCode, data, resultCode) == true) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
