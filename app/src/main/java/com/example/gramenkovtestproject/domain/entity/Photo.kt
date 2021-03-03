package com.example.gramenkovtestproject.domain.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Photo(
    var albumId: Int? = null,
    @PrimaryKey var id: Int? = null,
    var title: String? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null,
) : RealmObject()
