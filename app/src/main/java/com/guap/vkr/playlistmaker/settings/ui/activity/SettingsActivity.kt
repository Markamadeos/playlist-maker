package com.guap.vkr.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.btn_back)
        val darkThemeButton = findViewById<SwitchCompat>(R.id.btn_dark_theme)
        val shareButton = findViewById<TextView>(R.id.btn_share)
        val feedbackButton = findViewById<TextView>(R.id.btn_feedback)
        val licenseButton = findViewById<TextView>(R.id.btn_license)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(this)
        )[SettingsViewModel::class.java]

        viewModel.themeLiveData.observe(this) {
            darkThemeButton.isChecked = it
        }

        backButton.setOnClickListener {
            finish()
        }

        darkThemeButton.setOnCheckedChangeListener { _, checked ->
            viewModel.setupTheme(checked)
        }

        shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        feedbackButton.setOnClickListener {
            viewModel.openSupport()
        }

        licenseButton.setOnClickListener {
            viewModel.openTerms()
        }
    }
}