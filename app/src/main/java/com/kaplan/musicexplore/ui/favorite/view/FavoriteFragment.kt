package com.kaplan.musicexplore.ui.favorite.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.databinding.FragmentFavoriteBinding
import com.kaplan.musicexplore.di.Injectable
import com.kaplan.musicexplore.di.injectViewModel
import com.kaplan.musicexplore.di.observe
import com.kaplan.musicexplore.util.ConnectivityUtil
import com.kaplan.musicexplore.util.ui.hide
import com.kaplan.musicexplore.util.ui.show
import javax.inject.Inject


class FavoriteFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteViewModel = injectViewModel(viewModelFactory)
        favoriteViewModel.connectivityAvailable =
            ConnectivityUtil.isNetworkAvailable(requireContext())
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.isEmpty = favoriteAdapter.favoriteItems.isNullOrEmpty()
        binding.isPlaying = false
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = favoriteAdapter
            binding.viewModel = favoriteViewModel
            favoriteAdapter.viewModelAdapter = favoriteViewModel
        }
        subscribeUi(binding, favoriteAdapter)
        observePlay()
    }

    private fun subscribeUi(binding: FragmentFavoriteBinding, adapter: FavoriteAdapter) {
        observe(favoriteViewModel.getLikedTracks())
        { result ->
            when (result.status) {
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (!result.data.isNullOrEmpty()) {
                        binding?.apply {
                            isEmpty = false
                            adapter.updateData(result.data)
                            executePendingBindings()
                        }
                    }
                }
            }
        }
    }

    private fun observePlay() {
        observe(favoriteViewModel.songPair)
        { pair ->

            if (this::player.isInitialized.not()) {
                initializePlayer()
            }
            var isPlaying = pair.first
            binding.isPlaying = isPlaying
            var previewUrl = pair.second
            if (isPlaying) {
                setMediaItem(previewUrl)
                prepare()
                play()
            } else {
                pause()
            }
            binding.executePendingBindings()
        }
    }

    private fun initializePlayer() {
        // Instantiate the player.
        player = SimpleExoPlayer.Builder(requireContext()).build()
        // Attach player to the view.
        binding.playerControlView.player = player
    }

    // player methods
    private fun setMediaItem(url: String) = player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
    private fun play() = player.play()
    private fun prepare() = player.prepare()
    private fun pause() = player.pause()
    private fun stop() = player.stop()

    override fun onDestroy() {
        super.onDestroy()
        if (this::player.isInitialized)
            stop()
            player.release()
    }
}