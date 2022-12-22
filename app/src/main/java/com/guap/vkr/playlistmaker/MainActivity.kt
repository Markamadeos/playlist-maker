package com.guap.vkr.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.btn_search)
        val libraryButton = findViewById<Button>(R.id.btn_library)
        val settingsButton = findViewById<Button>(R.id.btn_settings)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажата кнопка поиска!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        searchButton.setOnClickListener(imageClickListener)

        libraryButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка медиатеки!", Toast.LENGTH_SHORT).show()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка настроек!", Toast.LENGTH_SHORT).show()
        }
    }
}