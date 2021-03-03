package com.example.gramenkovtestproject.presentation.modules.album.modules.net.view

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.base.IBaseView

interface IAlbumFragment : IBaseView<List<Album>?> {
    fun hideSplash()
    fun showSplash()
    fun showNoInternet()
    fun hideNoInternet()
}