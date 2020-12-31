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
    @SerializedName("amgArtistId") val amgArtistId : Int,
    @SerializedName("artistName") val artistName : String,
    @SerializedName("collectionName") val collectionName : String,
    @SerializedName("collectionCensoredName") val collectionCensoredName : String,
    @SerializedName("artistViewUrl") val artistViewUrl : String,
    @SerializedName("collectionViewUrl") val collectionViewUrl : String,
    @SerializedName("artworkUrl60") val artworkUrl60 : String,
    @SerializedName("artworkUrl100") val artworkUrl100 : String,
    @SerializedName("collectionPrice") val collectionPrice : Double,
    @SerializedName("collectionExplicitness") val collectionExplicitness : String,
    @SerializedName("contentAdvisoryRating") @ColumnInfo(defaultValue = "")  val contentAdvisoryRating : String,
    @SerializedName("trackCount") val trackCount : Int,
    @SerializedName("copyright") val copyright : String,
    @SerializedName("country") val country : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("releaseDate") val releaseDate : Date,
    @SerializedName("primaryGenreName") val primaryGenreName : String)
