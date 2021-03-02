package com.example.gramenkovtestproject.data.local

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.DataCompletion

interface IPhotoRepositoryLocal {
    fun getSavedAlbums(completion: DataCompletion<MutableList<Album>>)
    fun savePhotos(album: Album?, list: List<Photo>, completion: DataCompletion<Boolean>)
    fun getSavedPhotos(albumId: Int, completion: DataCompletion<MutableList<Photo>>)
    fun deleteAlbum(albumId: Int,completion: DataCompletion<Boolean>)
}