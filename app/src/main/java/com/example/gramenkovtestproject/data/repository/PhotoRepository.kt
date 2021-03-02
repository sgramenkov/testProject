package com.example.gramenkovtestproject.data.repository

import com.example.gramenkovtestproject.data.network.PhotoService
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.DataCompletion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val service: PhotoService) : IPhotoRepository {

    override fun getPhotos(id: Int, completion: DataCompletion<List<Photo>?>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getPhotos(id = id)
                withContext(Dispatchers.Main) {

                    if (response.isSuccessful)
                        completion(response.body(), null)
                    else
                        completion(null, response.message())

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    completion(null, e.message)
                }
            }
        }
    }

    override fun getAlbums(completion: DataCompletion<List<Album>?>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getAlbums()

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful)
                        completion(response.body(), null)
                    else
                        completion(null, response.message())

                }


            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    completion(null, e.message)
                }
            }

        }
    }
}