package com.kaplan.musicexplore.ui.track.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.databinding.FragmentTracksBinding
import com.kaplan.musicexplore.di.Injectable
import com.kaplan.musicexplore.di.injectViewModel
import com.kaplan.musicexplore.di.observe
import com.kaplan.musicexplore.ui.track.data.Track
import com.kaplan.musicexplore.util.ConnectivityUtil
import com.kaplan.musicexplore.util.EndlessScrollModel
import com.kaplan.musicexplore.util.ui.hide
import com.kaplan.musicexplore.util.ui.show
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class TracksFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tracksViewModel: TracksViewModel

    private lateinit var binding: FragmentTracksBinding

    @Inject
    lateinit var tracksAdapter: TracksAdapter

    private val endlessScrollModel = EndlessScrollModel()
    var firstLoad = true
    var trackId = ""
    private val args: TracksFragmentArgs by navArgs()

    private var likedIds = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tracksViewModel = injectViewModel(viewModelFactory)
        tracksViewModel.connectivityAvailable =
            ConnectivityUtil.isNetworkAvailable(requireContext())
        tracksAdapter.setViewModel(tracksViewModel)
        binding = FragmentTracksBinding.inflate(inflater, container, false)
        binding.isEmpty = tracksAdapter.trackItems.isNullOrEmpty()
        binding.executePendingBindings()
        return binding.root
    }

    private fun setupArgs() {
        args.let { fragmentArgs ->
            fragmentArgs.trackId?.let {
                trackId = it
                tracksViewModel.onLoadMore(trackId)
                subscribeUi(binding, tracksAdapter)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollModel.visibleThreshold = 20
        binding.apply {
            recyclerView.adapter = tracksAdapter
            binding.viewModel = tracksViewModel
            model = endlessScrollModel
        }
        setupArgs()
        subscribeFavorites()
    }

    private fun subscribeFavorites() {
        observe(tracksViewModel.getLikedTracks())
        { likedTracks ->
            likedIds = likedTracks.map { favorite ->
                favorite.trackId
            }.toMutableList()
        }
    }

    private fun subscribeUi(binding: FragmentTracksBinding, adapter: TracksAdapter) {
        observe(tracksViewModel.listMediator)
        { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (!result.data.isNullOrEmpty()) {
                        binding?.apply {
                            isEmpty = false
                            result.data[0].collectionName?.let {
                                requireActivity().toolbar?.title = it
                            }
                            adapter.updateData(selectTracksFromList(result.data))
                            executePendingBindings()
                        }
                    }
                }
                Result.Status.LOADING -> {
                    binding?.apply {
                        progressBar.show()
                        if (firstLoad) {
                            isEmpty = true
                            executePendingBindings()
                        }
                    }
                    firstLoad = false
                }
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    binding?.apply {
                        isEmpty = false
                        executePendingBindings()
                    }

                    binding.progressBar.hide()
                    binding.root?.let {
                        Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun selectTracksFromList(tracks: List<Track>): List<Track> {
        tracks.onEach { track ->
            if (likedIds.any { it == track.trackId }) {
                track.isLiked = true
            }
        }
        return tracks.filter { track -> track.wrapperType == "track" }
    }
}