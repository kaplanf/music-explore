package com.kaplan.musicexplore.ui.artist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey @SerializedName("artistId") val artistId: Int,
    @SerializedName("wrapperType") @ColumnInfo(defaultValue = "")  val wrapperType: String,
    @SerializedName("artistType") @ColumnInfo(defaultValue = "")  val artistType: String,
    @SerializedName("artistName") @ColumnInfo(defaultValue = "")  val artistName: String,
    @SerializedName("artistLinkUrl") @ColumnInfo(defaultValue = "")  val artistLinkUrl: String,
    @SerializedName("amgArtistId") val amgArtistId: Int,
    @SerializedName("primaryGenreName") @ColumnInfo(defaultValue = "")  val primaryGenreName: String,
    @SerializedName("primaryGenreId") val primaryGenreId: Int,
    var searchQuery: String
)

