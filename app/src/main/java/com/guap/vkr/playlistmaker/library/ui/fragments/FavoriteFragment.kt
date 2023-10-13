package com.guap.vkr.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentFavoriteBinding
import com.guap.vkr.playlistmaker.library.ui.view_model.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.ivEmptyList.setImageResource(R.drawable.ic_search_err)
        binding.tvEmptyList.text = getString(R.string.empty_favorite_text)
        return binding.root
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}