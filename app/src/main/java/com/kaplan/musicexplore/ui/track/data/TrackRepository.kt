package com.kaplan.musicexplore.ui.track.data

import com.kaplan.musicexplore.data.resultLiveData
import com.kaplan.musicexplore.ui.favorite.data.Favorite
import com.kaplan.musicexplore.ui.favorite.data.FavoriteDao
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepository @Inject constructor(
    private val dao: TrackDao, private val favoriteDao: FavoriteDao,
    private val trackRemoteDataSource: TrackRemoteDataSource
) {

    fun observeTracks(id: String, offset: Int) = resultLiveData(
        databaseQuery = { dao.getTrackbyId(id) },
        networkCall = { trackRemoteDataSource.getTracks(id, offset) },
        saveCallResult = { dao.insertAll(it.results.filter { track -> track.trackName.isNullOrEmpty().not() })}
    )

    suspend fun saveTrack(track: Track) {
        favoriteDao.saveFavorite(convertTrackToFavorite(track))
    }

    fun getLikedTracks() = favoriteDao.getLikedTracks()

    fun convertTrackToFavorite(track: Track): Favorite = Favorite(track.trackId,  Calendar.getInstance().time, track.trackName, track.trackViewUrl, track.previewUrl, track.artworkUrl100, track.trackTimeMillis)
}
