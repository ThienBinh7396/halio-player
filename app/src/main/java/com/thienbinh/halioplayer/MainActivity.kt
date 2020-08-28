package com.thienbinh.halioplayer

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    startMusicService()

    play.setOnClickListener {
      val intent = Intent()

      val bundle = Bundle()
      bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, Music.getMusicById(1))

      intent.action = ACTION_MUSIC_UPDATE
      intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

      sendBroadcast(intent)
    }

    toggle.setOnClickListener {
      val intent = Intent()
      intent.action = ACTION_MUSIC_TOGGLE
      sendBroadcast(intent)
    }
  }

  private fun startMusicService() {
    val intent = Intent(this@MainActivity, MusicService::class.java)
    intent.action = ACTION_MUSIC_INITIALIZE
    startService(intent)
  }
}


