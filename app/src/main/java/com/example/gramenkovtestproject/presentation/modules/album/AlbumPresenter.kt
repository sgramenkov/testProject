package com.example.gramenkovtestproject.presentation.modules.album

import com.example.gramenkovtestproject.data.repository.IPhotoRepository
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.base.BasePresenter
import com.example.gramenkovtestproject.presentation.base.IBaseView
import com.example.gramenkovtestproject.presentation.modules.photo.IPhotoPresenter
import javax.inject.Inject

class AlbumPresenter @Inject constructor(private val repo: IPhotoRepository) : IAlbumPresenter,
    BasePresenter<IAlbumFragment>() {

    override fun getAlbums() {
        repo.getAlbums(completion = { data, error ->
            if (error != null) getView()?.onError(error)
            if (data != null) getView()?.onResult(data)
        })
    }

}