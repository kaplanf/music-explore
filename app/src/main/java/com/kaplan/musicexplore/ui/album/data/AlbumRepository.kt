package com.kaplan.musicexplore.ui.album.data

import com.kaplan.musicexplore.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepository @Inject constructor(
    private val dao: AlbumDao,
    private val albumRemoteDataSource: AlbumRemoteDataSource
) {

    fun observeAlbums(id: String, offset: Int) = resultLiveData(
        databaseQuery = { dao.getAlbumbyId(id) },
        networkCall = { albumRemoteDataSource.getAlbums(id, offset) },
        saveCallResult = { dao.insertAll(it.results.filter { album -> album.collectionType.isNullOrEmpty().not() })}
    )
}
