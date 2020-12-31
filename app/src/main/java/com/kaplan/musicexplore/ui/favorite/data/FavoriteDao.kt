package com.kaplan.musicexplore.ui.favorite.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaplan.musicexplore.ui.track.data.Track

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY createdDate DESC")
    fun getLikedTracks(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(favorite: Favorite)
}