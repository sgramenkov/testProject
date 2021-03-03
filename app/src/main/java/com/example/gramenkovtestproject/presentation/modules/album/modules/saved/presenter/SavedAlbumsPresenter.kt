package com.example.gramenkovtestproject.presentation.modules.album.modules.saved.presenter

import com.example.gramenkovtestproject.data.local.IPhotoRepositoryLocal
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view.ISavedAlbumsFragment
import javax.inject.Inject

class SavedAlbumsPresenter @Inject constructor(private val repo: IPhotoRepositoryLocal) :
    ISavedAlbumsPresenter, BasePresenter<ISavedAlbumsFragment>() {

    override fun getSavedAlbums() {
        repo.getSavedAlbums { data, error ->
            if (error != null)
                getView()?.onError(error)
            if (data != null)
                getView()?.onResult(data)
        }
    }
}