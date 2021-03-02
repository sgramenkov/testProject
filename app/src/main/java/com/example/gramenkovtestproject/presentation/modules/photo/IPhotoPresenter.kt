package com.example.gramenkovtestproject.presentation.modules.photo

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo

interface IPhotoPresenter {
    fun getPhotos(albumId: Int)
    fun savePhotos(album: Album?, list: List<Photo>)
    fun getSavedPhotos(albumId: Int)
    fun deleteAlbum(albumId: Int)
}