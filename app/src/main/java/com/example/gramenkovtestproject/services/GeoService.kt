package com.example.gramenkovtestproject.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.coordinatorlayout.R
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

import com.example.gramenkovtestproject.App
import com.example.gramenkovtestproject.domain.utils.Keys
import com.example.gramenkovtestproject.domain.utils.Keys.LAT
import com.example.gramenkovtestproject.domain.utils.Keys.LNG
import com.example.gramenkovtestproject.domain.utils.Keys.PROVIDER_ENABLED
import com.example.gramenkovtestproject.presentation.base.MainActivity
import com.google.android.gms.location.*

class GeoService : Service() {

    private var isMusicPlaying = false

    private val player = MediaPlayer()

    private var locationRequest: LocationRequest? = null
    private var client: FusedLocationProviderClient? = null
    private lateinit var callbackGeo: LocationCallback

    private val intent = Intent().apply {
        action = GEO_ACTION
    }

    private val UPDATE_INTERVAL = 1000L
    private val FASTEST_INTERVAL = 1000L

    private var lastLat = 0.0
    private var lastLng = 0.0

    private val notificatonManager =
        App.ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val GEO_ACTION = "com.gramenkov.geo"
        var isRunning = false
        const val CHANNEL_ID = "1"
        const val NOTIFICATION_ID_SERVICE = 10
    }

    override fun onCreate() {
        super.onCreate()
        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }
        initLocationListener()
    }

    private fun initLocationListener() {
        val builder = LocationSettingsRequest.Builder()

        locationRequest?.let {
            builder.addLocationRequest(it)

            val locationSettingsRequest = builder.build()

            val settingsClient = LocationServices.getSettingsClient(applicationContext)

            settingsClient.checkLocationSettings(locationSettingsRequest)
            client = LocationServices.getFusedLocationProviderClient(applicationContext)
            callbackGeo = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    val location = p0?.lastLocation

                    val lat = location?.latitude ?: 0.0
                    val lng = location?.longitude ?: 0.0

                    val lngDiff = kotlin.math.abs(lastLng - lng)
                    val latDiff = kotlin.math.abs(lastLat - lat)

                    if (lat != 0.0 && lng != 0.0 && lastLat != 0.0 && lastLng != 0.0 && (lngDiff != 0.0 || latDiff != 0.0)) {
                        intent.apply {
                            putExtra(LAT, lat)
                            putExtra(LNG, lng)
                            putExtra(PROVIDER_ENABLED, true)
                        }

                        val notification = buildNotification(
                            title = "Current location",
                            content = "Lat: $lat, Lng: $lng",
                            subText = javaClass.simpleName,
                            isAutoCancel = false
                        )

                        notificatonManager.notify(NOTIFICATION_ID_SERVICE, notification)
                        sendBroadcast(intent)
                    }

                    lastLat = lat
                    lastLng = lng
                }
            }
        }
    }

    private fun playMusic() {
        if (!isMusicPlaying) {
            isMusicPlaying = true

            val asd = assets.openFd("music.mp3")
            player.apply {
                setDataSource(asd.fileDescriptor, asd.startOffset, asd.length)

                setOnCompletionListener {
                    isMusicPlaying = false
                    stopPlayer()
                }

                prepare()
                start()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        init()

        val notification = buildNotification(
            title = getString(com.example.gramenkovtestproject.R.string.current_loc),
            content = getString(com.example.gramenkovtestproject.R.string.finding_your_coords),
            subText = javaClass.simpleName,
            isAutoCancel = false
        )

        notificatonManager.notify(NOTIFICATION_ID_SERVICE, notification)
        startForeground(NOTIFICATION_ID_SERVICE, notification)
        isRunning = true
        playMusic()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(
        title: String,
        content: String,
        subText: String?,
        isAutoCancel: Boolean,
    ): Notification {
        val intent = Intent(this, MainActivity::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = NotificationChannel(CHANNEL_ID, "Channel", NotificationManager.IMPORTANCE_HIGH)
            notificatonManager.createNotificationChannel(nc)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(isAutoCancel)
            .setSubText(subText)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setColor(Color.parseColor("#1152FD"))
            .setPriority(Notification.PRIORITY_MAX)

        return notification.build()
    }

    override fun onDestroy() {
        isRunning = false
        stopPlayer()
        client?.removeLocationUpdates(callbackGeo)
        Toast.makeText(App.ctx, getString(com.example.gramenkovtestproject.R.string.service_stopped), Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun stopPlayer() {
        player.stop()
        player.reset()
        isMusicPlaying = false
    }

    override fun onLowMemory() {
        Toast.makeText(applicationContext, getString(com.example.gramenkovtestproject.R.string.low_memory), Toast.LENGTH_LONG).show()
        stopPlayer()
        super.onLowMemory()
    }

    private fun init() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        client?.requestLocationUpdates(locationRequest, callbackGeo, Looper.myLooper())
    }

}

