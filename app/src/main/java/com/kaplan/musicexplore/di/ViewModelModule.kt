package com.kaplan.musicexplore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaplan.musicexplore.ui.album.view.AlbumsViewModel
import com.kaplan.musicexplore.ui.artist.view.ArtistsViewModel
import com.kaplan.musicexplore.ui.favorite.view.FavoriteViewModel
import com.kaplan.musicexplore.ui.track.view.TracksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArtistsViewModel::class)
    abstract fun bindArtistsViewModel(viewModel: ArtistsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumsViewModel::class)
    abstract fun bindAlbumbsViewModel(viewModel: AlbumsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TracksViewModel::class)
    abstract fun bindTracksViewModel(viewModel: TracksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
