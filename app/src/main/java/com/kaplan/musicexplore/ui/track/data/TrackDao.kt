package com.kaplan.musicexplore.ui.track.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listItems: List<Track>)

    @Query("SELECT * FROM tracks WHERE collectionId = :id")
    fun getTrackbyId(id: String): LiveData<List<Track>>
}