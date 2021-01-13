package com.kaplan.musicexplore.ui.album.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "albums")
data class Album(
    @SerializedName("wrapperType") val wrapperType : String,
    @SerializedName("collectionType") val collectionType : String,
    @SerializedName("artistId") val artistId : Int,
    @PrimaryKey @SerializedName("collectionId") val collectionId : Int,
    @SerializedName("artistName") val artistName : String,
    @SerializedName("collectionName") val collectionName : String,
    @SerializedName("artworkUrl100") val artworkUrl100 : String?,
    @SerializedName("contentAdvisoryRating") @ColumnInfo(defaultValue = "")  val contentAdvisoryRating : String,
    @SerializedName("releaseDate") val releaseDate : Date,
    @SerializedName("primaryGenreName") val primaryGenreName : String)
