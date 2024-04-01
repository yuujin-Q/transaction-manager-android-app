package com.mog.bondoman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.ActivityMainBinding
import com.mog.bondoman.service.JwtService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var jwtServiceIntent: Intent
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sessionManager = SessionManager.getInstance(
            applicationContext.getSharedPreferences(
                SessionManager.PREF_KEY,
                MODE_PRIVATE
            )
        )
//        SessionManager.initialize(
//            applicationContext.getSharedPreferences(
//                SessionManager.PREF_KEY,
//                MODE_PRIVATE
//            )
//        )

        jwtServiceIntent = Intent(this, JwtService::class.java)
        setContentView(binding.root)

        startService(jwtServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(jwtServiceIntent)
    }
}