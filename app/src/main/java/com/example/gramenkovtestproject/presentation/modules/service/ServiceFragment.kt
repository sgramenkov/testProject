package com.example.gramenkovtestproject.presentation.modules.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.gramenkovtestproject.databinding.FragmentServiceBinding
import com.example.gramenkovtestproject.presentation.base.MainActivity
import com.example.gramenkovtestproject.services.GeoService
import com.example.gramenkovtestproject.services.GeoService.Companion.GEO_ACTION


class ServiceFragment : Fragment() {

    private lateinit var binding: FragmentServiceBinding

    private lateinit var br: BroadcastReceiver

    private lateinit var gpsEnabledReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(inflater, container, false)

        val gpsEnabledFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        gpsEnabledFilter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        val manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?

        val isProviderEnabled =
            manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

        if (!isProviderEnabled) {
            onGpsDisable()
        } else {
            onGpsEnable("0.0", "0.0")
        }

        gpsEnabledReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val m = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?
                if (m?.isProviderEnabled(LocationManager.GPS_PROVIDER) != true) {
                    onGpsDisable()
                } else {
                    onGpsEnable("0.0", "0.0")
                }
            }
        }

        requireActivity().registerReceiver(gpsEnabledReceiver, gpsEnabledFilter)


        val intent = Intent(requireContext(), GeoService::class.java)

        br = object : BroadcastReceiver() {
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

        requireActivity().registerReceiver(br, IntentFilter(GEO_ACTION))



        if (GeoService.isRunning) {
            binding.btn.text = "Stop Service"
        } else {
            binding.btn.text = "Start Service"
        }

        binding.btn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (!GeoService.isRunning) {
                    activity?.startService(intent)
                    binding.btn.text = "Stop Service"
                } else {
                    activity?.stopService(intent)
                    binding.btn.text = "Start service"
                }
            } else {
                (activity as MainActivity).reqGeoPerm()
            }

        }



        return binding.root
    }

    private fun onGpsDisable() {
        binding.et.setText("Turn on gps module")
        binding.btn.alpha = 0.5f
        binding.btn.text = "Start Service"
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