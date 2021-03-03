package com.example.gramenkovtestproject.presentation.modules.photo.presenter

import com.example.gramenkovtestproject.data.local.IPhotoRepositoryLocal
import com.example.gramenkovtestproject.data.repository.IPhotoRepository
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.domain.utils.NetworkConnection
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import com.example.gramenkovtestproject.presentation.modules.photo.presenter.IPhotoPresenter
import com.example.gramenkovtestproject.presentation.modules.photo.view.IPhotoActivity
import javax.inject.Inject

class PhotoPresenter @Inject constructor(
    private val repo: IPhotoRepository,
    private val localRepo: IPhotoRepositoryLocal
) : IPhotoPresenter,
    BasePresenter<IPhotoActivity>() {

    override fun getPhotos(albumId: Int) {
        if (NetworkConnection.isInternetAvailable()) {
            getView()?.hideNoInternet()
            getView()?.showSplash()

            repo.getPhotos(albumId) { data, error ->
                getView()?.hideSplash()
                if (error != null) getView()?.onError(error)
                if (data != null) {
                    getView()?.isFromRealm(false)
                    getView()?.onResult(data)
                }
            }
        } else {
            getView()?.showNoInternet()
            getView()?.onError(null)
        }

    }

    override fun onSaveAlbumBtnClick(album: Album?, list: List<Photo>) {
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
            getView()?.hideSplash()
            getView()?.hideNoInternet()
            if (error != null)
                getView()?.onError(error)
            if (data != null) {
                getView()?.isFromRealm(true)
                getView()?.onResult(data)
            }
        }
    }

    override fun onDeleteBtnClick(albumId: Int) {
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