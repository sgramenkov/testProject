package com.example.gramenkovtestproject.presentation.modules.album

import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.base.IBaseView

interface IPhotoActivity : IBaseView<List<Photo>?> {
    fun onSuccess()
    fun setDeleteResult()
    fun changeSaveBtn()
    fun lockActionBtn()
    fun unlockActionBtn()
    fun isFromRealm(status: Boolean)
}
