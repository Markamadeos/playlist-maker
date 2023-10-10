package com.guap.vkr.playlistmaker.library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.guap.vkr.playlistmaker.databinding.ActivityLibraryBinding
import com.guap.vkr.playlistmaker.library.ui.LibraryViewPagerAdapter

class LibraryActivity : AppCompatActivity() {

    private var _binding: ActivityLibraryBinding? = null
    private val binding get() = _binding!!
    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator get() = _tabMediator!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        binding.btnBack.setOnClickListener {
            finish()
        }

        _tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                FAVORITE_FRAGMENT -> tab.text = "Избранные треки"
                PLAYLIST_FRAGMENT -> tab.text = "Плейлисты"
            }
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _tabMediator?.detach()
    }

    companion object {
        const val FAVORITE_FRAGMENT = 0
        const val PLAYLIST_FRAGMENT = 1
    }

}
