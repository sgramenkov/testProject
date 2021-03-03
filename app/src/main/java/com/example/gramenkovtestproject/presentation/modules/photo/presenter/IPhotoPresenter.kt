package com.example.gramenkovtestproject.presentation.modules.photo.presenter

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo

interface IPhotoPresenter {
    fun getPhotos(albumId: Int)
    fun onSaveAlbumBtnClick(album: Album?, list: List<Photo>)
    fun getSavedPhotos(albumId: Int)
    fun onDeleteBtnClick(albumId: Int)
    fun getFullSizePhoto(albumId: Int, id: Int)
}