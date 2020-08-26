package com.thienbinh.halioplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    Handler(Looper.getMainLooper()).postDelayed({
      val intent = Intent(this@SplashActivity, MainActivity::class.java)
      startActivity(intent)

      finish()
    }, 1000)

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)
  }
}