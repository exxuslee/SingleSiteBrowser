package com.cookiejarapps.android.smartcookieweb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.cookiejarapps.android.smartcookieweb.databinding.ActivityMainBinding
import com.cookiejarapps.android.smartcookieweb.ext.components
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.contextmenu.ext.DefaultSelectionActionDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val browserFragment: BrowserFragment?
        get() = supportFragmentManager.findFragmentById(R.id.browser_fragment) as? BrowserFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            windowInsets
        }

        val url = intent.getStringExtra(EXTRA_URL) ?: BuildConfig.SITE_URL
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    R.id.browser_fragment,
                    BrowserFragment().apply {
                        arguments = Bundle().apply {
                            putString(BrowserFragment.ARG_URL, url)
                        }
                    },
                )
            }
        } else {
            browserFragment?.loadUrl(url)
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (browserFragment?.onBackPressed() != true) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
                    }
                }
            },
        )
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.getStringExtra(EXTRA_URL)?.let { url ->
            browserFragment?.loadUrl(url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (browserFragment?.onActivityResult(requestCode, data, resultCode) == true) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val EXTRA_URL = "extra_url"
    }
}
