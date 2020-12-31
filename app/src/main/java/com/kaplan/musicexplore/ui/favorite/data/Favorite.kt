package com.kaplan.musicexplore.ui.favorite.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey var trackId: Int,
    var createdDate: Date,
    val trackName: String,
    val trackViewUrl: String,
    val previewUrl: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long
)