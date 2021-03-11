package com.example.gramenkovtestproject.presentation.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gramenkovtestproject.presentation.modules.album.modules.net.view.AlbumFragment
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view.SavedAlbumsFragment
import com.example.gramenkovtestproject.presentation.modules.geo.ServiceFragment

class VpAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragList = listOf(AlbumFragment(), SavedAlbumsFragment(), ServiceFragment())

    override fun getCount(): Int {
        return fragList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragList[position]
    }

}