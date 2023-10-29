package com.guap.vkr.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.ActivityRootBinding
import com.guap.vkr.playlistmaker.search.ui.SearchFragment

class RootActivity : AppCompatActivity() {

    private var _binding: ActivityRootBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Добавляем фрагмент в контейнер
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, SearchFragment())
            }
        }
    }
}
