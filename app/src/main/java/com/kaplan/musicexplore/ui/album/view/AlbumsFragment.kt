package com.kaplan.musicexplore.ui.album.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.databinding.FragmentAlbumsBinding
import com.kaplan.musicexplore.databinding.FragmentArtistsBinding
import com.kaplan.musicexplore.di.Injectable
import com.kaplan.musicexplore.di.injectViewModel
import com.kaplan.musicexplore.di.observe
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.artist.view.ArtistAdapter
import com.kaplan.musicexplore.ui.artist.view.ArtistsViewModel
import com.kaplan.musicexplore.util.ConnectivityUtil
import com.kaplan.musicexplore.util.EndlessScrollModel
import com.kaplan.musicexplore.util.ui.hide
import com.kaplan.musicexplore.util.ui.show
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AlbumsFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var albumsViewModel: AlbumsViewModel

    private lateinit var binding: FragmentAlbumsBinding

    @Inject
    lateinit var albumsAdapter: AlbumsAdapter

    private val endlessScrollModel = EndlessScrollModel()
    var firstLoad = true
    var artistId = ""
    private val args: AlbumsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        albumsViewModel = injectViewModel(viewModelFactory)
        albumsViewModel.connectivityAvailable =
            ConnectivityUtil.isNetworkAvailable(requireContext())
        binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        binding.isEmpty = albumsAdapter.albumItems.isNullOrEmpty()
        binding.executePendingBindings()
        return binding.root
    }

    private fun setupArgs() {
        args.let { fragmentArgs ->
            fragmentArgs.artistId?.let {
                artistId = it
                albumsViewModel.onLoadMore(artistId)
                subscribeUi(binding, albumsAdapter)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollModel.visibleThreshold = 20
        binding.apply {
            recyclerView.adapter = albumsAdapter
            binding.viewModel = albumsViewModel
            model = endlessScrollModel
        }
        setupArgs()
    }

    private fun subscribeUi(binding: FragmentAlbumsBinding, adapter: AlbumsAdapter) {
        observe(albumsViewModel.listMediator)
        { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    if (!result.data.isNullOrEmpty()) {
                        binding?.apply {
                            isEmpty = false
                            result.data[0].artistName?.let {
                                requireActivity().toolbar?.title = it
                            }
                            adapter.updateData(selectAlbumsFromList(result.data))
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

    fun selectAlbumsFromList(albums: List<Album>): List<Album> {
        return albums.filter { album -> album.collectionType == "Album" }
    }
}