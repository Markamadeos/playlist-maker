package com.guap.vkr.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guap.vkr.playlistmaker.databinding.FragmentSettingsBinding
import com.guap.vkr.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeLiveData.observe(viewLifecycleOwner) {
            binding.btnDarkTheme.isChecked = it
        }

        setupButtons()

    }

    private fun setupButtons() {
        with(binding) {
            btnDarkTheme.setOnCheckedChangeListener { _, checked ->
                viewModel.setupTheme(checked)
            }

            btnShare.setOnClickListener {
                viewModel.shareApp()
            }

            btnFeedback.setOnClickListener {
                viewModel.openSupport()
            }

            btnLicense.setOnClickListener {
                viewModel.openTerms()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}