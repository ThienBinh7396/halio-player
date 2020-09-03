package com.thienbinh.halioplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music

class SplashActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    Genre.createInstance()
    Music.initializeList()
    Genre.mapMusicToGenre()

    Handler(Looper.getMainLooper()).postDelayed({
      val intent = Intent(this@SplashActivity, MainActivity::class.java)
      startActivity(intent)
      finish()
    }, 1000)
  }
}