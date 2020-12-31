package com.kaplan.musicexplore.di

import com.kaplan.musicexplore.ui.album.view.AlbumsFragment
import com.kaplan.musicexplore.ui.artist.view.ArtistsFragment
import com.kaplan.musicexplore.ui.favorite.view.FavoriteFragment
import com.kaplan.musicexplore.ui.track.view.TracksFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeArtistsFragment(): ArtistsFragment

    @ContributesAndroidInjector
    abstract fun contributeAlbumsFragment(): AlbumsFragment

    @ContributesAndroidInjector
    abstract fun contributeTracksFragment(): TracksFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment
}
