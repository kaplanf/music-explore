package com.kaplan.musicexplore.ui.artist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey @SerializedName("artistId") val artistId: Int,
    @SerializedName("wrapperType") val wrapperType: String,
    @SerializedName("artistType") val artistType: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("artistLinkUrl") val artistLinkUrl: String,
    @SerializedName("amgArtistId") val amgArtistId: Int,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("primaryGenreId") val primaryGenreId: Int,
    var searchQuery: String,
)
