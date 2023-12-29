package com.guap.vkr.playlistmaker.library.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guap.vkr.playlistmaker.R
import com.guap.vkr.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.guap.vkr.playlistmaker.library.domain.model.Playlist
import com.guap.vkr.playlistmaker.library.ui.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var coverUri: Uri? = Uri.EMPTY
    private lateinit var closeModalWindow: MaterialAlertDialogBuilder
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                coverUri = uri
                setCover()
            }
        }

    private fun isDataFilled(): Boolean {
        return coverUri != Uri.EMPTY
                || !binding.etPlaylistName.text.isNullOrEmpty()
                || !binding.etPlaylistDiscription.text.isNullOrEmpty()
    }

    private fun showExitConfirmDialog() {
        if (isDataFilled()) {
            closeModalWindow = MaterialAlertDialogBuilder(requireContext())
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

    private fun getInternalStorageCoverName(uri: Uri?): String {
        return if (uri != Uri.EMPTY) {
            viewModel.saveCover(uri)
        } else R.drawable.ic_album_placeholder_2x.toString()
    }

    private fun pickCover() {
        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setCover() {
        Glide.with(this)
            .load(coverUri)
            .centerCrop()
            .transform(
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
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun playlistCreatedShowNotification() {
        Snackbar.make(
            binding.root,
            getString(R.string.playlist_created, playlistName),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun bind() {
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
                    Playlist(
                        playlistId = null,
                        playlistName = playlistName,
                        playlistDescription = playlistDescription,
                        imgUri = getInternalStorageCoverName(coverUri)
                    )
                )
                playlistCreatedShowNotification()
                findNavController().popBackStack()
            }
            etPlaylistName.doAfterTextChanged {
                playlistName = it.toString()
            }
            etPlaylistDiscription.doAfterTextChanged {
                playlistDescription = it.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}