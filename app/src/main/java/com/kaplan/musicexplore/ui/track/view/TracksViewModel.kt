package com.kaplan.musicexplore.ui.track.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.di.CoroutineScropeIO
import com.kaplan.musicexplore.ui.track.data.Track
import com.kaplan.musicexplore.ui.track.data.TrackRepository
import com.kaplan.musicexplore.util.default
import com.kaplan.musicexplore.util.then
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    val repository: TrackRepository,
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {
    var connectivityAvailable: Boolean = false
    val pageLiveData = MutableLiveData<Int>() default 0
    val offset: Int get() = (pageLiveData.value!! > 0) then (pageLiveData.value!!) * 20 ?: 0
    val trackIdLiveData = MutableLiveData<Long>() default 0

    var mutablelist = MutableLiveData<Result<List<Track>>>()
    val list: LiveData<Result<List<Track>>> get() = mutablelist
    val listMediator = MediatorLiveData<Result<List<Track>>>()

    fun onLoadMore(id: String) {
        id?.let {
            trackIdLiveData.value = id.toLong()
            listMediator.addSource(getTracks(id)) {
                listMediator.postValue(it)
            }
        }
    }

    fun getLikedTracks() = repository.getLikedTracks()

    fun getTracks(id: String) = repository.observeTracks(id, offset)

    fun setPage(page: Int) {
        pageLiveData.value = page
        trackIdLiveData.value?.let {
            onLoadMore(it.toString())
        }
    }

    fun insertTrack(track: Track) {
        ioCoroutineScope.launch {
            repository.saveTrack(track)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}