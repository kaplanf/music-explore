package com.kaplan.musicexplore.ui.artist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists ORDER BY artistId ASC")
    fun getAllArtists(): LiveData<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listItems: List<Artist>)

    @Query("SELECT * FROM artists WHERE artistId = :id")
    fun getArtistbyId(id: String): LiveData<Artist>

    @Query("SELECT * FROM artists WHERE searchQuery = :artistName")
    fun getArtistbyName(artistName: String): LiveData<List<Artist>>
}