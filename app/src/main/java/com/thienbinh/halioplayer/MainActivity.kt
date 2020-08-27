package com.thienbinh.halioplayer

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    startMusicService()

    play.setOnClickListener {
      val intent = Intent()
      intent.action = ACTION_MUSIC_UPDATE
      intent.putExtra(ACTION_MUSIC_SOURCE_DATA, "file_small.mp3")
      sendBroadcast(intent)
    }

    toggle.setOnClickListener {
      val intent = Intent()
      intent.action = ACTION_MUSIC_TOGGLE
      sendBroadcast(intent)
    }

    next.setOnClickListener {
      val intent = Intent()
      intent.action = ACTION_MUSIC_UPDATE
      intent.putExtra(ACTION_MUSIC_SOURCE_DATA, "let_me_down_slowly.mp3")
      sendBroadcast(intent)
    }
  }

  private fun startMusicService(){
    val intent = Intent(this@MainActivity, MusicService::class.java)
    intent.action = ACTION_MUSIC_INITIALIZE
    startService(intent)
  }
}


