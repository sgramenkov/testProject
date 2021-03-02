package com.example.gramenkovtestproject.data.local

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.DataCompletion
import io.realm.Realm
import io.realm.RealmList

class PhotoRepositoryLocal : IPhotoRepositoryLocal {

    var realm: Realm = Realm.getDefaultInstance()

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
        realm.executeTransactionAsync({
            it.insertOrUpdate(album)
            val realmList = RealmList<Photo>()
            realmList.addAll(list.asIterable())
            it.insertOrUpdate(realmList)
        }, {
            completion(true, null)
        }, {
            completion(false, "Error saving data")
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

        }, {
            completion(true, null)
        }, {
            completion(false, "Delete error")
        })
    }
}