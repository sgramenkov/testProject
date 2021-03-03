package com.example.gramenkovtestproject.presentation.modules.album.modules.saved.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.databinding.FragmentSavedBinding
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.presentation.modules.photo.view.PhotoActivity
import com.example.gramenkovtestproject.presentation.modules.photo.view.PhotoActivity.Companion.PHOTO_CODE
import com.example.gramenkovtestproject.presentation.modules.album.adapter.AlbumAdapter
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.presenter.ISavedAlbumsPresenter
import com.example.gramenkovtestproject.presentation.modules.album.modules.saved.presenter.SavedAlbumsPresenter
import javax.inject.Inject

class SavedAlbumsFragment : Fragment(), AlbumAdapter.AlbumItemListener, ISavedAlbumsFragment {

    private lateinit var binding: FragmentSavedBinding


    private lateinit var adapter: AlbumAdapter

    @Inject
    lateinit var presenter: ISavedAlbumsPresenter

    init {
        App.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater, container, false)

        (presenter as SavedAlbumsPresenter).attachView(this)

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
        if (data.isEmpty()) {
            binding.noContent.visibility = View.VISIBLE
            binding.savedAlbumsRv.visibility = View.GONE
        } else {
            binding.noContent.visibility = View.GONE
            binding.savedAlbumsRv.visibility = View.VISIBLE
        }
        adapter.addItems(data)
    }

    override fun onError(err: String?) {
        if (err != null)
            Toast.makeText(requireContext(), err, Toast.LENGTH_LONG).show()
    }

    override fun onAlbumClick(album: Album?) {
        activity?.startActivityForResult(Intent(requireContext(), PhotoActivity::class.java).apply {
            putExtra("album", album)
            putExtra("savedData", true)
        }, PHOTO_CODE)
    }

}