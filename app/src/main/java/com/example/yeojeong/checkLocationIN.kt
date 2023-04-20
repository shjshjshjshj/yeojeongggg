package com.example.yeojeong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.yeojeong.databinding.ActivityCheckLocationInBinding
import com.example.yeojeong.databinding.ActivityMainBinding
import com.example.yeojeong.ui.placedetail.placedetailPage


class checkLocationIN : AppCompatActivity() {

    private lateinit var binding: ActivityCheckLocationInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_location_in)

        binding = ActivityCheckLocationInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide();

        val homebutton = binding.homeButton
        val placebutton = binding.placeButton

        val intent = Intent(this, MainActivity::class.java)
        val homeintent = Intent(this, placedetailPage::class.java)

        homebutton.setOnClickListener {
            startActivity(intent)
        }
        placebutton.setOnClickListener {
            startActivity(homeintent)
        }

    }
}