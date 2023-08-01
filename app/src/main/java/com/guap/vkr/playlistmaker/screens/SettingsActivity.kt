package com.guap.vkr.playlistmaker.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guap.vkr.playlistmaker.App
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivitySettingsBinding
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import com.guap.vkr.playlistmaker.utils.THEME_SWITCH_KEY

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }

//        val backButton = findViewById<ImageView>(R.id.btn_back)
//        val darkThemeButton = findViewById<SwitchCompat>(R.id.btn_dark_theme)
//        val shareButton = findViewById<TextView>(R.id.btn_share)
//        val feedbackButton = findViewById<TextView>(R.id.btn_feedback)
//        val licenseButton = findViewById<TextView>(R.id.btn_license)
        val sharedPref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDarkTheme.isChecked = sharedPref.getBoolean(THEME_SWITCH_KEY, false)
        binding.btnDarkTheme.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            // TODO(пофиксить положение свича, при мереходе между экранами сбрасывется его состояние)
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plan"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_url))
            }
            startActivity(shareIntent)
        }

        binding.btnFeedback.setOnClickListener {
            val feedbackIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.letter_topic)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.letter_body)
                )
            }
            startActivity(feedbackIntent)
        }

        binding.btnLicense.setOnClickListener {
            val licenseIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.license_url))
            }
            startActivity(licenseIntent)
        }
    }
}