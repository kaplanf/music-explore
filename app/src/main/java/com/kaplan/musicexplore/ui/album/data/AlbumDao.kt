package com.kaplan.musicexplore.ui.album.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums ORDER BY collectionId ASC")
    fun getAllAlbums(): LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listItems: List<Album>)

    @Query("SELECT * FROM albums WHERE artistId = :id")
    fun getAlbumbyId(id: String): LiveData<List<Album>>
}