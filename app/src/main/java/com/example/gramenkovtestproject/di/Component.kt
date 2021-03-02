package com.example.gramenkovtestproject.di

import com.example.gramenkovtestproject.presentation.modules.album.AlbumFragment
import com.example.gramenkovtestproject.presentation.modules.album.PhotoActivity
import com.example.gramenkovtestproject.presentation.modules.database.DatabaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, PhotoModule::class])
interface Component {
    fun inject(frag: AlbumFragment)
    fun inject(act: PhotoActivity)
    fun inject(frag: DatabaseFragment)
}