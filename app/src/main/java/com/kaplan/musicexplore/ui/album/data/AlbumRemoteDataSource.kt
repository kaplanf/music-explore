package com.kaplan.musicexplore.ui.album.data

import com.kaplan.musicexplore.api.BaseDataSource
import com.kaplan.musicexplore.api.WebService
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(private val service: WebService) : BaseDataSource() {

    suspend fun getAlbums(id: String, offset: Int) = getResult { service.getAlbums(id, offset) }
}