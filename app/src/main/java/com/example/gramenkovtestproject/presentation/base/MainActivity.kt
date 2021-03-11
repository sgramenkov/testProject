package com.example.gramenkovtestproject.presentation.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.gramenkovtestproject.R
import com.example.gramenkovtestproject.databinding.ActivityMainBinding
import com.example.gramenkovtestproject.presentation.base.adapter.VpAdapter
import com.example.gramenkovtestproject.presentation.modules.album.modules.net.view.AlbumFragment
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view.ISavedAlbumsFragment
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view.SavedAlbumsFragment
import com.example.gramenkovtestproject.presentation.modules.geo.IServiceFragment
import com.example.gramenkovtestproject.presentation.modules.geo.ServiceFragment
import com.example.gramenkovtestproject.presentation.modules.photo.view.PhotoActivity.Companion.PHOTO_CODE
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var vp: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        vp = findViewById(R.id.main_vp)

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
        if (requestCode == 11 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val frag = supportFragmentManager.findFragmentByTag("SRVC") as? IServiceFragment
            frag?.onPermissionGranted()
        }
    }

    private fun firstFragmentInit() {
        vp.offscreenPageLimit = 3
        vp.adapter = VpAdapter(supportFragmentManager)
    }

    private fun bottomNavListener() {
        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.album -> vp.setCurrentItem(0, false)

                R.id.database -> vp.setCurrentItem(1, false)

                R.id.service -> vp.setCurrentItem(2, false)
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_CODE && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            val frag = supportFragmentManager.findFragmentByTag("DB") as? ISavedAlbumsFragment
            frag?.updateData()
        }
    }

}
