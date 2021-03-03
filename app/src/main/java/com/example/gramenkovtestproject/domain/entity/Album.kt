package com.example.gramenkovtestproject.domain.entity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Album(
    @PrimaryKey var id: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
) : RealmObject(), Serializable


open class SavedPhoto(
    var id: Int? = null,
    var albumId: Int? = null,
    var drawable: ByteArray? = null
) : RealmObject()