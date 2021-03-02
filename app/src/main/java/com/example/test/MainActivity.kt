package com.example.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.navView.setOnNavigationItemSelectedListener {
            Toast.makeText(this, "Click", Toast.LENGTH_LONG).show()
            true
        }
        setContentView(R.layout.activity_main)

    }

}