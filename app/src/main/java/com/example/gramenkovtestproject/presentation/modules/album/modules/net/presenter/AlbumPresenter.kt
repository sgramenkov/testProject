package com.example.gramenkovtestproject.presentation.modules.album.modules.net.presenter

import com.example.gramenkovtestproject.data.repository.IPhotoRepository
import com.example.gramenkovtestproject.domain.utils.NetworkConnection
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import com.example.gramenkovtestproject.presentation.modules.album.modules.net.view.IAlbumFragment
import javax.inject.Inject

class AlbumPresenter @Inject constructor(private val repo: IPhotoRepository) : IAlbumPresenter,
    BasePresenter<IAlbumFragment>() {

    override fun getAlbums() {
        if (NetworkConnection.isInternetAvailable()) {
            getView()?.hideNoInternet()
            getView()?.showSplash()
            repo.getAlbums(completion = { data, error ->
                getView()?.hideSplash()

                if (error != null) getView()?.onError(error)
                if (data != null) getView()?.onResult(data)
            })
        } else {
            getView()?.showNoInternet()
            getView()?.onError(null)
        }
    }

}