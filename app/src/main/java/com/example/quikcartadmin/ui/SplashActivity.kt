package com.example.quikcartadmin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.quikcartadmin.databinding.ActivitySplashBinding
import com.example.quikcartadmin.helpers.AuthHelper
import com.example.quikcartadmin.ui.auth.view.AuthenticationActivity

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashLottie.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = if (AuthHelper.isUserSignedIn(this)) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, AuthenticationActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)

    }
}