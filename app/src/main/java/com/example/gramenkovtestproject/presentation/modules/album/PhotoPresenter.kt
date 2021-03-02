package com.example.gramenkovtestproject.presentation.modules.album

import com.example.gramenkovtestproject.data.local.IPhotoRepositoryLocal
import com.example.gramenkovtestproject.data.repository.IPhotoRepository
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import com.example.gramenkovtestproject.presentation.modules.photo.IPhotoPresenter
import javax.inject.Inject

class PhotoPresenter @Inject constructor(
    private val repo: IPhotoRepository,
    private val localRepo: IPhotoRepositoryLocal
) : IPhotoPresenter,
    BasePresenter<IPhotoActivity>() {

    override fun getPhotos(albumId: Int) {
        repo.getPhotos(albumId) { data, error ->
            if (error != null) getView()?.onError(error)
            if (data != null) {
                getView()?.isFromRealm(false)
                getView()?.onResult(data)
            }
        }
    }

    override fun savePhotos(album: Album?, list: List<Photo>) {
        getView()?.lockActionBtn()
        localRepo.savePhotos(album, list, completion = { isSuccess, error ->
            getView()?.unlockActionBtn()
            if (error != null) {
                getView()?.onError(error)
            }
            if (isSuccess == true) {
                getView()?.changeSaveBtn()
                getView()?.onSuccess()
            }
        })
    }

    override fun getSavedPhotos(albumId: Int) {
        localRepo.getSavedPhotos(albumId) { data, error ->
            if (error != null)
                getView()?.onError(error)
            if (data != null) {
                getView()?.isFromRealm(true)
                getView()?.onResult(data)
            }
        }
    }

    override fun deleteAlbum(albumId: Int) {
        getView()?.lockActionBtn()
        localRepo.deleteAlbum(albumId) { data, error ->
            getView()?.unlockActionBtn()
            if (error != null)
                getView()?.onError(error)
            if (data == true) {
                getView()?.setDeleteResult()
                getView()?.onSuccess()
            }
        }
    }
}