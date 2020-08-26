package com.thienbinh.halioplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)
  }
}