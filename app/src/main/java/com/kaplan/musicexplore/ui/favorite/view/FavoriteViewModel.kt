package com.kaplan.musicexplore.ui.favorite.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaplan.musicexplore.di.CoroutineScropeIO
import com.kaplan.musicexplore.ui.favorite.data.FavoriteRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    val repository: FavoriteRepository,
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

    private var songPairMutable: MutableLiveData<Pair<Boolean, String>> = MutableLiveData<Pair<Boolean, String>>()
    val songPair get() = songPairMutable

    var connectivityAvailable: Boolean = false

    fun getLikedTracks() = repository.getLikedTracks()

    fun sendSongPlayStatus(previewUrl: String, playing: Boolean) =  songPairMutable.postValue(Pair(playing, previewUrl))

}