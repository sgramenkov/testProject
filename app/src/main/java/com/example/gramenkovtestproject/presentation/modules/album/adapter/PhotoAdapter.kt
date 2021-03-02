package com.example.gramenkovtestproject.presentation.modules.album.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.databinding.ItemPhotoBinding
import com.example.gramenkovtestproject.domain.entity.Photo

class PhotoAdapter(private val list: MutableList<Photo>?, private val listener: PhotoItemListener) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = list?.get(position)

            val glideUrl = GlideUrl(
                item?.thumbnailUrl,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", WebSettings.getDefaultUserAgent(App.ctx)).build()
            )
            Glide.with(App.ctx).load(glideUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageProgress.alpha = 0f
                        return false
                    }
                }).into(binding.iv)

            binding.titleTv.isSelected = true
            binding.titleTv.movementMethod = ScrollingMovementMethod()

            binding.titleTv.text = item?.title

            itemView.setOnClickListener {
                listener.onPhotoClick(item?.id ?: -1,item?.url?:"")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoViewHolder(ItemPhotoBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = list?.size ?: 0

    interface PhotoItemListener {
        fun onPhotoClick(id: Int, url: String)
    }

    fun addItems(items: List<Photo>?) {
        items?.let {
            list?.addAll(it)
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun getItems(): List<Photo>? {
        return list
    }

}

class SquareLayout(context: Context, attributeSet: AttributeSet? = null) :
    CardView(context, attributeSet) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}