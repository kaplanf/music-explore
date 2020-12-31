package com.kaplan.musicexplore.api

import com.kaplan.musicexplore.ui.album.data.Album
import com.kaplan.musicexplore.ui.artist.data.Artist
import com.kaplan.musicexplore.ui.track.data.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WebService {

    companion object {
        const val ENDPOINT = "https://itunes.apple.com/"
    }

    @GET("search?entity=musicArtist&limit=20")
    suspend fun searchArtist(@Query("term") artistName: String, @Query("offset") offset: Int? = null): Response<ResultsResponse<Artist>>

    @GET("lookup?entity=album&limit=20")
    suspend fun getAlbums(@Query("id") id: String,  @Query("offset") offset: Int? = null) : Response<ResultsResponse<Album>>

    @GET("lookup?entity=song&limit=20")
    suspend fun getTrack(@Query("id") id: String, @Query("offset") offset: Int? = null) : Response<ResultsResponse<Track>>
}
