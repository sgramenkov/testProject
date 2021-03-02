package com.example.gramenkovtestproject.presentation.modules.database

import com.example.gramenkovtestproject.data.local.IPhotoRepositoryLocal
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import javax.inject.Inject

class DatabasePresenter @Inject constructor(private val repo: IPhotoRepositoryLocal) :
    IDatabasePresenter, BasePresenter<IDatabaseFragment>() {

    override fun getSavedAlbums() {
        repo.getSavedAlbums { data, error ->
            if (error != null)
                getView()?.onError(error)
            if (data != null)
                getView()?.onResult(data)
        }
    }
}