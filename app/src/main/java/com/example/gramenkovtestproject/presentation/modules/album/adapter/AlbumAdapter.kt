package com.example.gramenkovtestproject.presentation.modules.album.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gramenkovtestproject.databinding.ItemAlbumBinding
import com.example.gramenkovtestproject.domain.entity.Album

class AlbumAdapter(private val list: MutableList<Album>?, private val listener: AlbumItemListener) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list?.get(position)
            binding.titleTv.text = item?.title

            itemView.setOnClickListener {
                listener.onAlbumClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlbumViewHolder(ItemAlbumBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = list?.size ?: 0

    fun addItems(items: List<Album>?) {
        Handler().post {
            list?.clear()
            items?.let {
                list?.addAll(it)
            }
            notifyDataSetChanged()
        }
    }

    interface AlbumItemListener {
        fun onAlbumClick(album: Album?)
    }
}