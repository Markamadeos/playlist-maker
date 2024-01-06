package com.guap.vkr.playlistmaker.library.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.guap.vkr.playlistmaker.library.ui.fragments.FavoriteFragment
import com.guap.vkr.playlistmaker.library.ui.fragments.PlaylistsFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = FRAGMENTS_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            FIRST_POSITION -> FavoriteFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    companion object {
        const val FRAGMENTS_COUNT = 2
        const val FIRST_POSITION = 0
    }

}