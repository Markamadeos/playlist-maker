package com.guap.vkr.playlistmaker.library.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.guap.vkr.playlistmaker.library.ui.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    val binding get() = _binding!!
    val viewModel by viewModel<NewPlaylistViewModel>()
    open var coverUri: Uri? = Uri.EMPTY
    private lateinit var closeModalWindow: MaterialAlertDialogBuilder
    open var playlistName: String = ""
    open var playlistDescription: String = ""
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                coverUri = uri
                setCover()
            }
        }
    open val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitConfirmDialog()
        }
    }

    private fun isDataFilled(): Boolean {
        return coverUri != Uri.EMPTY
                || !binding.etPlaylistName.text.isNullOrEmpty()
                || !binding.etPlaylistDescription.text.isNullOrEmpty()
    }

    private fun showExitConfirmDialog() {
        if (isDataFilled()) {
            closeModalWindow =
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                    .setTitle(getString(R.string.finalize_the_playlist))
                    .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(getString(R.string.complete)) { _, _ ->
                        findNavController().popBackStack()
                    }
            closeModalWindow.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun pickCover() {
        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setCover() {
        Glide.with(requireContext())
            .load(coverUri)
            .placeholder(R.drawable.ic_album_placeholder_2x)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    requireContext().resources.getDimensionPixelSize(R.dimen._8dp)
                )
            )
            .into(binding.ivPlaylistPicture)
        binding.ivPlaylistPicturePlaceholder.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    open fun playlistCreatedShowNotification() {
        Snackbar.make(
            binding.root,
            getString(R.string.playlist_created, playlistName),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    open fun bind() {
        with(binding) {
            btnBack.setOnClickListener {
                showExitConfirmDialog()
            }
            ivPlaylistPicture.setOnClickListener {
                pickCover()
            }
            etPlaylistName.doOnTextChanged { text, _, _, _ ->
                btnCreatePlaylist.isEnabled = !text.isNullOrBlank()
            }
            btnCreatePlaylist.setOnClickListener {
                viewModel.createPlaylist(
                    playlistName, playlistDescription, coverUri
                )
                playlistCreatedShowNotification()
                findNavController().popBackStack()
            }
            etPlaylistName.doAfterTextChanged {
                playlistName = it.toString()
            }
            etPlaylistDescription.doAfterTextChanged {
                playlistDescription = it.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}