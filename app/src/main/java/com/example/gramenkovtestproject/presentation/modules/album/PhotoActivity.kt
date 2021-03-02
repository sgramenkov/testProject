package com.example.gramenkovtestproject.presentation.modules.album

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.R
import com.example.gramenkovtestproject.databinding.ActivityPhotoBinding
import com.example.gramenkovtestproject.domain.entity.Album
import com.example.gramenkovtestproject.domain.entity.Photo
import com.example.gramenkovtestproject.presentation.modules.album.adapter.PhotoAdapter
import com.example.gramenkovtestproject.presentation.modules.photo.IPhotoPresenter
import javax.inject.Inject

class PhotoActivity : AppCompatActivity(), IPhotoActivity, PhotoAdapter.PhotoItemListener,
    MotionLayout.TransitionListener {

    private lateinit var binding: ActivityPhotoBinding

    private lateinit var adapter: PhotoAdapter

    private var isSavedData = false

    private var album: Album? = null

    private var isFromRealm = false
    private var isDataGot = false

    private var saveAlbumBtn: MenuItem? = null
    private var deleteAlbumBtn: MenuItem? = null

    private var isSliding = false

    private var isInPhotoView: Boolean = false

    @Inject
    lateinit var presenter: IPhotoPresenter

    init {
        App.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_photo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        (presenter as PhotoPresenter).attachView(this)

        adapter = PhotoAdapter(mutableListOf(), this)

        findViewById<RecyclerView>(R.id.photo_rv).adapter = adapter

        getDataFromArgs()

        presenter.getSavedPhotos(album?.id ?: -1)

        findViewById<MotionLayout>(R.id.motion).addTransitionListener(this)
    }

    override fun isFromRealm(status: Boolean) {
        isFromRealm = status
    }

    override fun onSuccess() {
        Log.e("realm", "Success")
    }

    private fun getDataFromArgs() {
        isSavedData = intent.getBooleanExtra("savedData", false)
        album = intent.getSerializableExtra("album") as? Album
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_album, menu)
        if (!isSavedData) {
            saveAlbumBtn = menu?.findItem(R.id.save_album)
        } else {
            deleteAlbumBtn = menu?.findItem(R.id.delete_album)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_album -> {
                val items = adapter.getItems() ?: listOf()
                presenter.savePhotos(album, items)
            }
            R.id.delete_album -> presenter.deleteAlbum(album?.id ?: -1)
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun lockActionBtn() {
        saveAlbumBtn?.isEnabled = false
        deleteAlbumBtn?.isEnabled = false
    }

    override fun unlockActionBtn() {
        saveAlbumBtn?.isEnabled = true
        deleteAlbumBtn?.isEnabled = true
    }

    override fun onPhotoClick(id: Int, url: String) {
        isInPhotoView = true
        val ml = findViewById<MotionLayout>(R.id.motion)
        findViewById<RecyclerView>(R.id.photo_rv).animate().alpha(0f).setDuration(300).start()
        ml.animate().alpha(1f).setDuration(300).withStartAction {
            ml.visibility = View.VISIBLE
        }.start()

        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(App.ctx)).build()
        )
        Glide.with(this).load(glideUrl).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Toast.makeText(this@PhotoActivity, "Fail $e", Toast.LENGTH_LONG).show()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).into(findViewById(R.id.full_size_iv))
    }

    override fun onBackPressed() {
        if (isInPhotoView) {
            val ml = findViewById<MotionLayout>(R.id.motion)
            findViewById<RecyclerView>(R.id.photo_rv).animate().alpha(1f).setDuration(300).start()
            ml.animate().alpha(0f).setDuration(300).withEndAction { ml.visibility = View.GONE }
                .start()
            isInPhotoView = false
        } else
            super.onBackPressed()
    }

    override fun onResult(data: List<Photo>?) {
        adapter.addItems(data)
        val handler = Handler()
        if (!isSavedData)
            handler.post {
                saveAlbumBtn?.isVisible = true
                if (isFromRealm && !data.isNullOrEmpty())
                    changeSaveBtn()
                else
                    if (!isDataGot) {
                        presenter.getPhotos(album?.id ?: -1)
                        isDataGot = true
                    }
            }
        else
            handler.post {
                deleteAlbumBtn?.isVisible = true
            }

    }

    override fun setDeleteResult() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onError(err: String) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show()
    }

    override fun changeSaveBtn() {
        saveAlbumBtn?.title = "Saved"
    }

    override fun onDestroy() {
        binding.motion.removeTransitionListener(this)
        super.onDestroy()
    }

    companion object {
        const val PHOTO_CODE = 10
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP && isSliding) {
            findViewById<ImageView>(R.id.iv).setImageDrawable(null)
            val ml = findViewById<MotionLayout>(R.id.motion)
            findViewById<RecyclerView>(R.id.photo_rv).animate().alpha(1f).setDuration(300).start()
            ml.animate().alpha(0f).setDuration(300).withEndAction {
                ml.visibility = View.GONE
                ml.progress = 0f
            }.start()

            isInPhotoView = false
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        isSliding = true
        Log.e("transaction", "start")
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        Log.e("transaction", "completed")
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        Log.e("transaction", "trigger")
    }
}