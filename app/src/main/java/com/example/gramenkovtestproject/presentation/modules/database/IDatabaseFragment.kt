package com.example.gramenkovtestproject.presentation.modules.database

import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.base.IBaseView

interface IDatabaseFragment : IBaseView<MutableList<Album>> {
    fun updateData()
}