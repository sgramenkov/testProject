package com.example.gramenkovtestproject.domain.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Album(
    @PrimaryKey var id: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
) : RealmObject(), Serializable
