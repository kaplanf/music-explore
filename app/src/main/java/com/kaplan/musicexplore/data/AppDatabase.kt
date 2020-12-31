package com.kaplan.musicexplore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaplan.musicexplore.data.converters.DateConverter
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.album.data.AlbumDao
import com.kaplan.musicexplore.ui.artist.data.Artist
import com.kaplan.musicexplore.ui.artist.data.ArtistDao
import com.kaplan.musicexplore.ui.favorite.data.Favorite
import com.kaplan.musicexplore.ui.favorite.data.FavoriteDao
import com.kaplan.musicexplore.ui.track.data.Track
import com.kaplan.musicexplore.ui.track.data.TrackDao

@Database(entities = [Artist::class, Album::class, Track::class, Favorite::class],
        version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun artistDao(): ArtistDao

    abstract fun albumDao(): AlbumDao

    abstract fun trackDao(): TrackDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "music-itunes")
                    .build()
        }
    }
}
