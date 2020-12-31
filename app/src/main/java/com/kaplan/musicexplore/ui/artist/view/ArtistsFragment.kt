package com.kaplan.musicexplore.ui.artist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kaplan.musicexplore.databinding.FragmentArtistsBinding
import com.kaplan.musicexplore.di.Injectable
import com.kaplan.musicexplore.di.injectViewModel
import com.kaplan.musicexplore.di.observe
import com.kaplan.musicexplore.util.ConnectivityUtil
import com.kaplan.musicexplore.util.EndlessScrollModel
import com.kaplan.musicexplore.util.ui.hide
import com.kaplan.musicexplore.util.ui.show
import javax.inject.Inject
import com.kaplan.musicexplore.data.Result


class ArtistsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var artistsViewModel: ArtistsViewModel

    private lateinit var binding: FragmentArtistsBinding

    @Inject
    lateinit var artistAdapter: ArtistAdapter

    private val endlessScrollModel = EndlessScrollModel()
    var firstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artistsViewModel = injectViewModel(viewModelFactory)
        artistsViewModel.connectivityAvailable =
            ConnectivityUtil.isNetworkAvailable(requireContext())
        binding = FragmentArtistsBinding.inflate(inflater, container, false)
        binding.isEmpty = artistAdapter.artistItems.isNullOrEmpty()
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollModel.visibleThreshold = 20
        binding.apply {
            recyclerView.adapter = artistAdapter
            binding.viewModel = artistsViewModel
            model = endlessScrollModel
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query.isNullOrEmpty().not() && query!!.length > 1)
                        artistsViewModel.onLoadMore(query)
                    subscribeUi(binding, artistAdapter)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        artistAdapter.updateData(emptyList())
                    }
                    return false
                }
            })
        }
    }

    private fun subscribeUi(binding: FragmentArtistsBinding, adapter: ArtistAdapter) {
        observe(artistsViewModel.listMediator)
        { result ->
            when (result.status) {
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
}