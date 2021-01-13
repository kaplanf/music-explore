package com.kaplan.musicexplore.ui.artist.data

import com.kaplan.musicexplore.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistRepository @Inject constructor(
    private val dao: ArtistDao,
    private val artistRemoteDataSource: ArtistRemoteDataSource
) {

    fun observeArtists(artistName: String, offset: Int) = resultLiveData(
        databaseQuery = { dao.getArtistbyName(artistName) },
        networkCall = { artistRemoteDataSource.searchArtists(artistName, offset) },
        saveCallResult = { it.results.onEach { artist -> artist.searchQuery = artistName }
            dao.insertAll(it.results) })

}
