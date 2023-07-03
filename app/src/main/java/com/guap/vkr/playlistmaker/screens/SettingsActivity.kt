package com.guap.vkr.playlistmaker.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SwitchCompat
import com.guap.vkr.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.btn_back)
        val darkThemeButton = findViewById<SwitchCompat>(R.id.btn_dark_theme)
        val shareButton = findViewById<TextView>(R.id.btn_share)
        val feedbackButton = findViewById<TextView>(R.id.btn_feedback)
        val licenseButton = findViewById<TextView>(R.id.btn_license)

        backButton.setOnClickListener {
            finish()
        }

        // в тз не требуется, но я попробовал сделать
        darkThemeButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plan"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_url))
            }
            startActivity(shareIntent)
        }

        feedbackButton.setOnClickListener {
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

        licenseButton.setOnClickListener {
            val licenseIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.license_url))
            }
            startActivity(licenseIntent)
        }
    }
}