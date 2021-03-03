package com.example.gramenkovtestproject.presentation.modules.photo.view

import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.IBaseView

interface IPhotoActivity : IBaseView<List<Photo>?> {
    fun onSuccess()
    fun setDeleteResult()
    fun changeSaveBtn()
    fun lockActionBtn()
    fun unlockActionBtn()
    fun isFromRealm(status: Boolean)
    fun hideSplash()
    fun showSplash()
    fun showNoInternet()
    fun hideNoInternet()
}
