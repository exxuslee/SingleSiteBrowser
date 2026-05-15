package com.cookiejarapps.android.smartcookieweb

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookiejarapps.android.smartcookieweb.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            windowInsets
        }

        val items = HomeItemRepository.loadItems(this)
        binding.sitesList.layoutManager = LinearLayoutManager(this)
        binding.sitesList.adapter = HomeItemsAdapter(items) { item ->
            when (item) {
                is HomeItem.Website -> openWebsite(item.url)
                is HomeItem.ExternalApp -> launchExternalApp(item.packageName)
            }
        }
    }

    private fun openWebsite(url: String) {
        startActivity(
            Intent(this, BrowserActivity::class.java).apply {
                putExtra(BrowserActivity.EXTRA_URL, url)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            },
        )
    }

    private fun launchExternalApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            try {
                startActivity(launchIntent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, R.string.target_not_found, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, R.string.target_not_installed, Toast.LENGTH_LONG).show()
        }
    }
}
