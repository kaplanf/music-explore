package com.kaplan.musicexplore.ui.favorite.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.track.data.Track

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listItems: List<Favorite>)

    @Query("SELECT * FROM favorites ORDER BY createdDate DESC")
    fun getLikedTracks(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}