package com.example.gramenkovtestproject.data.repository

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.DataCompletion

interface IPhotoRepository {
    fun getPhotos(id: Int, completion: DataCompletion<List<Photo>?>)
    fun getAlbums(completion: DataCompletion<List<Album>?>)
}