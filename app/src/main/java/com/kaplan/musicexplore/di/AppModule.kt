package com.kaplan.musicexplore.di

import android.app.Application
import android.content.Context
import com.kaplan.musicexplore.api.WebService
import com.kaplan.musicexplore.data.AppDatabase
import com.kaplan.musicexplore.ui.album.data.AlbumRemoteDataSource
import com.kaplan.musicexplore.ui.album.view.AlbumsAdapter
import com.kaplan.musicexplore.ui.artist.data.ArtistRemoteDataSource
import com.kaplan.musicexplore.ui.artist.view.ArtistAdapter
import com.kaplan.musicexplore.ui.favorite.view.FavoriteAdapter
import com.kaplan.musicexplore.ui.track.data.TrackRemoteDataSource
import com.kaplan.musicexplore.ui.track.view.TracksAdapter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, CoreDataModule::class])
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideWebService(
        @PrivateApi okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, WebService::class.java)

    @Singleton
    @Provides
    fun provideArtistRemoteDataSource(webService: WebService) =
        ArtistRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideAlbumRemoteDataSource(webService: WebService) =
        AlbumRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideTrackRemoteDataSource(webService: WebService) =
        TrackRemoteDataSource(webService)

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideArtistDao(db: AppDatabase) = db.artistDao()

    @Singleton
    @Provides
    fun provideAlbumDao(db: AppDatabase) = db.albumDao()

    @Singleton
    @Provides
    fun provideTrackDao(db: AppDatabase) = db.trackDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase) = db.favoriteDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Provides
    fun provideArtistAdapter(context: Context) = ArtistAdapter(context)

    @Provides
    fun provideAlbumsAdapter(context: Context) = AlbumsAdapter(context)


    @Provides
    fun provideTracksAdapter(context: Context) = TracksAdapter(context)

    @Provides
    fun provideFavoritesAdapter(context: Context) = FavoriteAdapter(context)

    @PrivateApi
    @Provides
    fun providePrivateOkHttpClient(
        upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            .build()
    }

    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WebService.ENDPOINT)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory, clazz: Class<T>
    ): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
}
