package com.example.gramenkovtestproject.presentation.modules.database

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.databinding.FragmentDatabaseBinding
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.modules.album.PhotoActivity
import com.example.gramenkovtestproject.presentation.modules.album.PhotoActivity.Companion.PHOTO_CODE
import com.example.gramenkovtestproject.presentation.modules.album.adapter.AlbumAdapter
import javax.inject.Inject

class DatabaseFragment : Fragment(), AlbumAdapter.AlbumItemListener, IDatabaseFragment {

    private lateinit var binding: FragmentDatabaseBinding


    private lateinit var adapter: AlbumAdapter

    @Inject
    lateinit var presenter: IDatabasePresenter

    init {
        App.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDatabaseBinding.inflate(inflater, container, false)

        (presenter as DatabasePresenter).attachView(this)

        adapter = AlbumAdapter(mutableListOf(), this)
        binding.savedAlbumsRv.adapter = adapter


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter.getSavedAlbums()
    }

    override fun updateData() {
        presenter.getSavedAlbums()
    }

    override fun onResult(data: MutableList<Album>) {
        adapter.addItems(data)
    }

    override fun onError(err: String) {
        Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
    }

    override fun onAlbumClick(album: Album?) {
        startActivityForResult(Intent(requireContext(), PhotoActivity::class.java).apply {
            putExtra("album", album)
            putExtra("savedData", true)
        }, PHOTO_CODE)
    }

}