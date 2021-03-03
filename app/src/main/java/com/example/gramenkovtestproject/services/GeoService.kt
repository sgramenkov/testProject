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
        locationRequest = LocationRequest()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = UPDATE_INTERVAL
        locationRequest?.fastestInterval = FASTEST_INTERVAL
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
                        Toast.makeText(App.ctx, "Location changed", Toast.LENGTH_SHORT).show()
                        playMusic()
                        intent.apply {
                            putExtra("lat", lat)
                            putExtra("long", lng)
                            putExtra("isProviderEnabled", true)
                        }

                        val notification = buildNotification(
                            "Current location",
                            "Lat: $lat, Lng: $lng",
                            javaClass.simpleName,
                            false
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
            player.setDataSource(asd.fileDescriptor, asd.startOffset, asd.length)
            player.setOnCompletionListener {
                isMusicPlaying = false
                stopPlayer()
            }
            player.prepare()
            player.start()

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        init()
        val notification =
            buildNotification(
                "Current location",
                "Finding your coords",
                javaClass.simpleName,
                false
            )

        notificatonManager.notify(NOTIFICATION_ID_SERVICE, notification)

        startForeground(NOTIFICATION_ID_SERVICE, notification)


        isRunning = true
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

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

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 0
        )

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
        Toast.makeText(App.ctx, "Service stopped", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun stopPlayer() {
        player.stop()
        player.reset()
        isMusicPlaying = false
    }

    override fun onLowMemory() {
        Toast.makeText(applicationContext, "Low memory", Toast.LENGTH_LONG).show()
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

