package com.example.gramenkovtestproject.di

import com.example.gramenkovtestproject.presentation.modules.album.modules.net.view.AlbumFragment
import com.example.gramenkovtestproject.presentation.modules.photo.view.PhotoActivity
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view.SavedAlbumsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, PhotoModule::class])
interface Component {
    fun inject(frag: AlbumFragment)
    fun inject(act: PhotoActivity)
    fun inject(frag: SavedAlbumsFragment)
}