package com.example.gramenkovtestproject.data.network

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {
    @GET("photos")
    suspend fun getPhotos(
        @Query("albumId") id: Int
    ): Response<List<Photo>?>

    @GET("albums")
    suspend fun getAlbums(): Response<List<Album>?>
}