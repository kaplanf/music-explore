package com.kaplan.musicexplore.ui.track.data

import com.kaplan.musicexplore.api.BaseDataSource
import com.kaplan.musicexplore.api.WebService
import javax.inject.Inject

class TrackRemoteDataSource @Inject constructor(private val service: WebService) : BaseDataSource() {

    suspend fun getTracks(id: String, offset: Int) = getResult { service.getTrack(id, offset) }
}