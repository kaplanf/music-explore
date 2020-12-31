package com.kaplan.musicexplore.ui.favorite.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.kaplan.musicexplore.data.Result
import com.kaplan.musicexplore.data.resultDbData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val dao: FavoriteDao,
    val context: Context
) {

    fun getLikedTracks(): LiveData<Result<List<Favorite>>> = resultDbData { dao.getLikedTracks() }


}