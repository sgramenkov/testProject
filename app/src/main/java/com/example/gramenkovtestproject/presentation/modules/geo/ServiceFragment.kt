package com.example.gramenkovtestproject.presentation.modules.geo

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.R
import com.example.gramenkovtestproject.databinding.FragmentServiceBinding
import com.example.gramenkovtestproject.presentation.base.MainActivity
import com.example.gramenkovtestproject.services.GeoService
import com.example.gramenkovtestproject.services.GeoService.Companion.GEO_ACTION


class ServiceFragment : Fragment(), IServiceFragment {

    private lateinit var binding: FragmentServiceBinding

    private lateinit var br: BroadcastReceiver

    private lateinit var gpsEnabledReceiver: BroadcastReceiver

    private var isMusicPlaying = false

    private val player = MediaPlayer()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(inflater, container, false)

        val gpsEnabledFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        gpsEnabledFilter.addAction(Intent.ACTION_PROVIDER_CHANGED)


        if (!isProviderEnabled()) {
            onGpsDisable()
        } else {
            onGpsEnable("0.0", "0.0")
        }

        gpsEnabledReceiver = initGpsEnableDisableReceiver()

        requireActivity().registerReceiver(gpsEnabledReceiver, gpsEnabledFilter)


        val intent = Intent(requireContext(), GeoService::class.java)

        br = initGeoReceiver()

        requireActivity().registerReceiver(br, IntentFilter(GEO_ACTION))

        if (GeoService.isRunning) {
            binding.btn.text = getString(R.string.stop_service)
        } else {
            binding.btn.text = getString(R.string.start_service)
        }

        binding.btn.setOnClickListener {
            if (isPermissionsGranted()) {
                if (!GeoService.isRunning) {
                    activity?.startService(intent)
                    binding.btn.text = getString(R.string.stop_service)
                } else {
                    activity?.stopService(intent)
                    binding.btn.text = getString(R.string.start_service)
                }
            } else {
                (activity as MainActivity).reqGeoPerm()
            }

        }



        return binding.root
    }

    private fun isProviderEnabled(): Boolean {
        val manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?

        return manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }

    private fun isPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initGpsEnableDisableReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!isProviderEnabled()) {
                    onGpsDisable()
                } else {
                    onGpsEnable("0.0", "0.0")
                }
            }
        }
    }

    private fun initGeoReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val lat = intent?.getDoubleExtra("lat", 0.0).toString()
                val long = intent?.getDoubleExtra("long", 0.0).toString()
                val isProviderEnabled = intent?.getBooleanExtra("isProviderEnabled", false)
                if (isProviderEnabled == true) {
                    onGpsEnable(lat, long)
                } else {
                    onGpsDisable()
                }
            }
        }
    }


    override fun onPermissionGranted() {
        val intent = Intent(requireContext(), GeoService::class.java)
        activity?.startService(intent)
        binding.btn.text = getString(R.string.stop_service)
    }

    private fun onGpsDisable() {
        binding.et.setText(getString(R.string.turn_on_gps))
        binding.btn.alpha = 0.5f
        binding.btn.text = getString(R.string.start_service)
        binding.btn.isEnabled = false
        activity?.stopService(Intent(requireContext(), GeoService::class.java))
    }

    private fun onGpsEnable(lat: String, long: String) {
        binding.btn.alpha = 1f
        binding.btn.isEnabled = true
        if (lat != "0.0" && long != "0.0") {
            bindEt(lat, long)
        }
    }


    private fun bindEt(lat: String, long: String) {
        binding.et.setText("Lat: $lat Long: $long")
    }


    override fun onDestroy() {
        requireActivity().unregisterReceiver(gpsEnabledReceiver)
        requireActivity().unregisterReceiver(br)
        super.onDestroy()
    }

}