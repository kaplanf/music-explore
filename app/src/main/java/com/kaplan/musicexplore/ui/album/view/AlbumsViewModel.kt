package com.kaplan.musicexplore.ui.album.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.di.CoroutineScropeIO
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.album.data.AlbumRepository
import com.kaplan.musicexplore.util.default
import com.kaplan.musicexplore.util.then
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class AlbumsViewModel @Inject constructor(
    val repository: AlbumRepository,
    @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel()

{
    var connectivityAvailable: Boolean = false
    val pageLiveData = MutableLiveData<Int>() default 0
    val offset: Int get() = (pageLiveData.value!! > 0) then (pageLiveData.value!!) * 20 ?: 0
    val albumIdLiveData = MutableLiveData<Long>() default 0

    var mutablelist = MutableLiveData<Result<List<Album>>>()
    val list: LiveData<Result<List<Album>>> get() = mutablelist
    val listMediator = MediatorLiveData<Result<List<Album>>>()

    fun onLoadMore(id: String) {
        id?.let {
            albumIdLiveData.value = id.toLong()
            listMediator.addSource(getAlbums(id)) {
                listMediator.postValue(it)
            }
        }
    }

    fun getAlbums(id: String) = repository.observeAlbums(id, offset)

    fun setPage(page: Int) {
        pageLiveData.value = page
        albumIdLiveData.value?.let {
            onLoadMore(it.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}

