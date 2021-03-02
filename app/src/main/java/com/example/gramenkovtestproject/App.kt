package com.example.gramenkovtestproject

import android.app.Application
import android.content.Context
import com.example.gramenkovtestproject.di.Component
import com.example.gramenkovtestproject.di.DaggerComponent
import io.realm.Realm

class App : Application() {
    companion object {
        lateinit var component: Component
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        ctx = applicationContext
        component = DaggerComponent.create()
    }
}