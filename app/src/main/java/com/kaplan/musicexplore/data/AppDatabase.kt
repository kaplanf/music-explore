package com.kaplan.musicexplore.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kaplan.musicexplore.data.converters.DateConverter
import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.album.data.AlbumDao
import com.kaplan.musicexplore.ui.artist.data.Artist
import com.kaplan.musicexplore.ui.artist.data.ArtistDao
import com.kaplan.musicexplore.ui.favorite.data.Favorite
import com.kaplan.musicexplore.ui.favorite.data.FavoriteDao
import com.kaplan.musicexplore.ui.track.data.Track
import com.kaplan.musicexplore.ui.track.data.TrackDao
import timber.log.Timber


@Database(
    entities = [Artist::class, Album::class, Track::class, Favorite::class],
    version = 2, exportSchema = false
)
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
            return Room.databaseBuilder(context, AppDatabase::class.java, "music-itunes").addMigrations(AppDatabase.MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
        }

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    val c = database.query("SELECT * FROM albums")
                    c.use {
                        if (c.moveToFirst()) {
                            val cv = ContentValues()
                            cv.put("wrapperType", c.getString(c.getColumnIndex("wrapperType")))
                            cv.put("collectionType", c.getString(c.getColumnIndex("collectionType")))
                            cv.put("artistId", c.getInt(c.getColumnIndex("artistId")))
                            cv.put("collectionId", c.getInt(c.getColumnIndex("collectionId")))
                            cv.put("artistName", c.getString(c.getColumnIndex("artistName")))
                            cv.put("collectionName", c.getString(c.getColumnIndex("collectionName")))
                            cv.put("artworkUrl100", c.getString(c.getColumnIndex("artworkUrl100")))
                            cv.put("contentAdvisoryRating", c.getString(c.getColumnIndex("contentAdvisoryRating")))
                            cv.put("releaseDate", c.getString(c.getColumnIndex("releaseDate")))
                            cv.put("primaryGenreName", c.getString(c.getColumnIndex("primaryGenreName")))
                            database.execSQL("DROP TABLE IF EXISTS `albums`")
                            createAlbumsTable(database)
                            database.insert("albums", 0, cv)
                        } else {
                            database.execSQL("DROP TABLE IF EXISTS `albums`")
                            createAlbumsTable(database)
                        }
                    }
                } catch (e: SQLiteException) {
                    Timber.e(e, "SQLiteException in migrate from database version 1 to version 2")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to migrate database version 1 to version 2")
                }
            }
        }

        fun createAlbumsTable(database: SupportSQLiteDatabase){
            database.execSQL("""CREATE TABLE IF NOT EXISTS `albums` (
                                `artistId` INTEGER NOT NULL,
                                `collectionId` INTEGER NOT NULL,
                                `wrapperType` TEXT NOT NULL,
                                `collectionType` TEXT NOT NULL,
                                `artistName` TEXT NOT NULL,
                                `collectionName` TEXT NOT NULL,
                                `artworkUrl100` TEXT,
                                `contentAdvisoryRating` TEXT NOT NULL DEFAULT '',
                                `releaseDate` INTEGER NOT NULL,
                                `primaryGenreName` TEXT NOT NULL,
                                PRIMARY KEY(`collectionId`))""".trimIndent())
        }
    }
}