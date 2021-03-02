package com.example.gramenkovtestproject.di

import com.example.gramenkovtestproject.data.local.IPhotoRepositoryLocal
import com.example.gramenkovtestproject.data.local.PhotoRepositoryLocal
import com.example.gramenkovtestproject.data.network.PhotoService
import com.example.gramenkovtestproject.data.repository.IPhotoRepository
import com.example.gramenkovtestproject.data.repository.PhotoRepository
import com.example.gramenkovtestproject.presentation.modules.album.AlbumPresenter
import com.example.gramenkovtestproject.presentation.modules.album.IAlbumPresenter
import com.example.gramenkovtestproject.presentation.modules.album.PhotoPresenter
import com.example.gramenkovtestproject.presentation.modules.database.DatabasePresenter
import com.example.gramenkovtestproject.presentation.modules.database.IDatabasePresenter
import com.example.gramenkovtestproject.presentation.modules.photo.IPhotoPresenter
import dagger.Module
import dagger.Provides

@Module
class PhotoModule {
    @Provides
    fun providePhotoRepo(service: PhotoService): IPhotoRepository = PhotoRepository(service)

    @Provides
    fun provideAlbumPresenter(repo: IPhotoRepository): IAlbumPresenter = AlbumPresenter(repo)

    @Provides
    fun providePhotoPresenter(
        repo: IPhotoRepository,
        localRepo: IPhotoRepositoryLocal
    ): IPhotoPresenter = PhotoPresenter(repo, localRepo)

    @Provides
    fun provideLocalPhotoRepo(): IPhotoRepositoryLocal = PhotoRepositoryLocal()

    @Provides
    fun provideDatabasePresenter(repo: IPhotoRepositoryLocal): IDatabasePresenter =
        DatabasePresenter(repo)
}