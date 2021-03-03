package com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.base.IBaseView

interface ISavedAlbumsFragment : IBaseView<MutableList<Album>> {
    fun updateData()
}