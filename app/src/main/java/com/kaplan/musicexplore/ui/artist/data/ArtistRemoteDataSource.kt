package com.kaplan.musicexplore.ui.artist.data

import com.kaplan.musicexplore.api.BaseDataSource
import com.kaplan.musicexplore.api.WebService
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(private val service: WebService) : BaseDataSource() {

    suspend fun searchArtists(artistName: String, offset: Int) = getResult { service.searchArtist(artistName, offset) }
}