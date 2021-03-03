package com.example.gramenkovtestproject.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebSettings
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.domain.entity.SavedPhoto
import com.example.gramenkovtestproject.presentation.base.DataCompletion
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PhotoRepositoryLocal : IPhotoRepositoryLocal {

    private var realm: Realm =
        Realm.getInstance(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())

    override fun getSavedAlbums(completion: DataCompletion<MutableList<Album>>) {
        var list = mutableListOf<Album>()
        realm.executeTransactionAsync({
            val realmRes = it.where(Album::class.java).findAll()
            list = it.copyFromRealm(realmRes)
        }, {
            completion(list, null)
        }, {
            completion(null, "Error getting data")
        })
    }

    override fun savePhotos(
        album: Album?,
        list: List<Photo>,
        completion: DataCompletion<Boolean>
    ) {

        CoroutineScope(Dispatchers.Default).launch {
            val photos = mutableListOf<SavedPhoto>()

            list.forEach {
                val glideUrl = GlideUrl(
                    it.url,
                    LazyHeaders.Builder()
                        .addHeader("User-Agent", WebSettings.getDefaultUserAgent(App.ctx))
                        .build()
                )
                val savedPhoto = SavedPhoto().apply {
                    this.albumId = it.albumId
                    this.id = it.id
                }


                val byteArrStream = ByteArrayOutputStream()
                val bitmap = Glide.with(App.ctx).asBitmap().load(glideUrl).submit().get()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrStream)
                savedPhoto.apply { drawable = byteArrStream.toByteArray() }

                photos.add(savedPhoto)
                Log.e("glide", "success ")
            }

            saveToRealm(album, photos, list) { data, error -> completion(data, error) }

        }
    }

    private fun saveToRealm(
        album: Album?,
        photos: List<SavedPhoto>,
        list: List<Photo>,
        completion: DataCompletion<Boolean>
    ) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            realm.executeTransactionAsync({

                val photosList = RealmList<SavedPhoto>()
                photosList.addAll(photos.asIterable())

                it.insertOrUpdate(album)

                it.insertOrUpdate(photosList)

                val realmList = RealmList<Photo>()
                realmList.addAll(list.asIterable())
                it.insertOrUpdate(realmList)
            }, {
                completion(true, null)
            }, {
                completion(false, "Error saving data")
            })
        }

        return
    }

    override fun getFullSizePhoto(albumId: Int, id: Int, completion: DataCompletion<Bitmap>) {
        var bitmap: Bitmap? = null
        realm.executeTransactionAsync({
            val photo =
                it.where(SavedPhoto::class.java).equalTo("albumId", albumId).equalTo("id", id)
                    .findFirst()

            val savedPhoto = it.copyFromRealm(photo)
            bitmap = BitmapFactory.decodeByteArray(
                savedPhoto?.drawable,
                0,
                savedPhoto?.drawable?.size ?: 0
            )


        }, {
            completion(bitmap, null)
        }, {
            completion(null, "Error getting photo")
        })
    }

    override fun getSavedPhotos(albumId: Int, completion: DataCompletion<MutableList<Photo>>) {
        var list = mutableListOf<Photo>()
        realm.executeTransactionAsync({
            val results = it.where(Photo::class.java).equalTo("albumId", albumId).findAll()
            list = it.copyFromRealm(results)
        }, {
            completion(list, null)
        }, {
            completion(null, "Error getting data")
        })
    }

    override fun deleteAlbum(albumId: Int, completion: DataCompletion<Boolean>) {
        realm.executeTransactionAsync({
            val album = it.where(Album::class.java).equalTo("id", albumId).findFirst()
            album?.deleteFromRealm()
            val photos = it.where(Photo::class.java).equalTo("albumId", albumId).findAll()
            photos.deleteAllFromRealm()
            val fullSizePhotos =
                it.where(SavedPhoto::class.java).equalTo("albumId", albumId).findAll()
            fullSizePhotos.deleteAllFromRealm()
        }, {
            completion(true, null)
        }, {
            completion(false, "Delete error")
        })
    }
}