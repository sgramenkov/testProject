package com.example.gramenkovtestproject.presentation.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.R
import com.example.gramenkovtestproject.databinding.ActivityMainBinding
import com.example.gramenkovtestproject.presentation.modules.album.AlbumFragment
import com.example.gramenkovtestproject.presentation.modules.album.PhotoActivity.Companion.PHOTO_CODE
import com.example.gramenkovtestproject.presentation.modules.database.DatabaseFragment
import com.example.gramenkovtestproject.presentation.modules.database.IDatabaseFragment
import com.example.gramenkovtestproject.presentation.modules.service.ServiceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val albumFragment: AlbumFragment = AlbumFragment()
    private val databaseFragment: DatabaseFragment = DatabaseFragment()
    private val serviceFragment: ServiceFragment = ServiceFragment()

    private var currentFrag: Fragment = albumFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        firstFragmentInit()
        bottomNavListener()
    }

     fun reqGeoPerm() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 11
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
    }

    private fun firstFragmentInit() {
        @IdRes val container = R.id.fragment_container
        val ft = supportFragmentManager.beginTransaction()

        if (!databaseFragment.isAdded)
            ft.add(container, databaseFragment, "DB").hide(databaseFragment)
        if (!serviceFragment.isAdded)
            ft.add(container, serviceFragment).hide(serviceFragment)
        if (!albumFragment.isAdded)
            ft.add(container, albumFragment).commit()
    }

    private fun bottomNavListener() {
        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener {
            Log.e("bottomnav", "click")
            when (it.itemId) {
                R.id.album -> {
                    if (currentFrag != albumFragment) {
                        supportFragmentManager.beginTransaction()
                            .show(albumFragment).hide(currentFrag).commit()
                        currentFrag = albumFragment
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.database -> {
                    if (currentFrag != databaseFragment) {
                        supportFragmentManager.beginTransaction()
                            .show(databaseFragment).hide(currentFrag).commit()
                        currentFrag = databaseFragment
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.service -> {
                    if (currentFrag != serviceFragment) {
                        supportFragmentManager.beginTransaction()
                            .show(serviceFragment).hide(currentFrag).commit()
                        currentFrag = serviceFragment
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_CODE && resultCode == RESULT_OK) {
            val frag = supportFragmentManager.findFragmentByTag("DB") as? IDatabaseFragment
            frag?.updateData()
        }
    }

}