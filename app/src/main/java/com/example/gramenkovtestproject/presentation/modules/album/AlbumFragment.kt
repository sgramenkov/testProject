package com.example.gramenkovtestproject.presentation.modules.album

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.databinding.FragmentAlbumBinding
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.modules.album.PhotoActivity.Companion.PHOTO_CODE
import com.example.gramenkovtestproject.presentation.modules.album.adapter.AlbumAdapter
import javax.inject.Inject

class AlbumFragment : Fragment(), IAlbumFragment, AlbumAdapter.AlbumItemListener {

    private lateinit var binding: FragmentAlbumBinding

    private lateinit var adapter: AlbumAdapter

    @Inject
    lateinit var presenter: IAlbumPresenter

    init {
        App.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        (presenter as AlbumPresenter).attachView(this)

        adapter = AlbumAdapter(mutableListOf(), this)

        binding.albumRv.adapter = adapter

        presenter.getAlbums()

        return binding.root
    }

    override fun onAlbumClick(album: Album?) {
        startActivityForResult(Intent(requireContext(), PhotoActivity::class.java).apply {
            putExtra("album", album)
            putExtra("savedData", false)
        }, PHOTO_CODE)
    }

    override fun onResult(data: List<Album>?) {
        adapter.addItems(data)
    }

    override fun onError(err: String) {
        Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
    }
}