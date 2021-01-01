package com.kaplan.musicexplore.ui.artist.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.di.CoroutineScropeIO
import com.kaplan.musicexplore.ui.artist.data.Artist
import com.kaplan.musicexplore.ui.artist.data.ArtistRepository
import com.kaplan.musicexplore.util.default
import com.kaplan.musicexplore.util.then
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class ArtistsViewModel @Inject constructor(
    val repository: ArtistRepository,
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

    var connectivityAvailable: Boolean = false
    private val pageLiveData = MutableLiveData<Int>() default 0
    val offset: Int get() = (pageLiveData.value!! > 0) then (pageLiveData.value!!) * 20 ?: 0
    private val artistNameLiveData = MutableLiveData<String>() default ""

    var mutablelist = MutableLiveData<Result<List<Artist>>>()
    val list: LiveData<Result<List<Artist>>> get() = mutablelist
    val listMediator = MediatorLiveData<Result<List<Artist>>>()

    fun onLoadMore(artistName: String) {
        artistName?.let {
            artistNameLiveData.value = artistName
            listMediator.addSource(getArtists(artistName)) {
                listMediator.postValue(it)
            }
        }
    }

    private fun getArtists(artistName: String) = repository.observeArtists(artistName, offset) as MutableLiveData<Result<List<Artist>>>

    fun setPage(page: Int) {
        pageLiveData.value = page
        artistNameLiveData.value?.let {
            onLoadMore(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}
