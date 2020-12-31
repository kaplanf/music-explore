package com.kaplan.musicexplore.ui.track.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tracks")
data class Track(
    @SerializedName("wrapperType") val wrapperType : String,
    @SerializedName("collectionId") val collectionId : Int,
    @PrimaryKey @SerializedName("trackId") val trackId : Int,
    @SerializedName("collectionName") val collectionName : String,
    @SerializedName("trackName") val trackName : String,
    @SerializedName("artistName") val artistName : String,
    @SerializedName("trackViewUrl") val trackViewUrl : String,
    @SerializedName("previewUrl") val previewUrl : String,
    @SerializedName("artworkUrl100") val artworkUrl100 : String,
    @SerializedName("collectionPrice") val collectionPrice : Double,
    @SerializedName("trackPrice") val trackPrice : Double,
    @SerializedName("trackExplicitness") val trackExplicitness : String,
    @SerializedName("contentAdvisoryRating") @ColumnInfo(defaultValue = "")  val contentAdvisoryRating : String,
    @SerializedName("trackCount") val trackCount : Int,
    @SerializedName("trackNumber") val trackNumber : Int,
    @SerializedName("trackTimeMillis") val trackTimeMillis : Long,
    @SerializedName("country") val country : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("isStreamable") val isStreamable : Boolean,
    var isLiked: Boolean
)
