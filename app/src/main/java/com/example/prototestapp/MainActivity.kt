package com.example.prototestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prototestapp.databinding.ActivityMainBinding

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Router.showMainFragmentMain(supportFragmentManager)
    }
}