package com.thienbinh.halioplayer.service

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.media.TimedMetaData
import android.net.Uri
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.WIFI_MODE_FULL_HIGH_PERF
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.notification.MusicInterfaceNotification
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import com.thienbinh.halioplayer.store.action.MusicAction
import java.lang.Error
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MusicService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
  private var isRunning = false

  private var mMediaPlayer: MediaPlayer? = null

  private var mMusicBroadcastReceiver: BroadcastReceiver? = null

  private var wifiLock: WifiManager.WifiLock? = null

  private var mScheduledExecutorService: ScheduledExecutorService? = null

  private var scheduledFuture: ScheduledFuture<*>? = null

  private val TIME_DELAY = 500L

  override fun onCreate() {
    super.onCreate()
    isRunning = true
    Log.d("Binh", "Service create")

    mMusicBroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        handleBroadcastReceiver(context, intent)
      }
    }

    val intentFilter = IntentFilter()

    intentFilter.addAction(ACTION_MUSIC_TOGGLE)
    intentFilter.addAction(ACTION_MUSIC_PLAY)
    intentFilter.addAction(ACTION_MUSIC_PAUSE)
    intentFilter.addAction(ACTION_MUSIC_UPDATE)

    registerReceiver(
      mMusicBroadcastReceiver, intentFilter
    )
  }

  private fun handleBroadcastReceiver(context: Context?, intent: Intent?) {
    if (mMediaPlayer == null) initializeMediaPlayer()

    intent?.apply {
      when (action) {
        ACTION_MUSIC_UPDATE -> {
          val checkDataReceiver = updateMusicFromIntent(context, this)
          if (checkDataReceiver) {
            mMediaPlayer!!.prepareAsync()
          }
        }

        ACTION_MUSIC_TOGGLE -> {

          Log.d("Binh", "Intent toggle")
          toggleStateMediaPlayer()
        }
      }
    }
  }

  private fun toggleStateMediaPlayer() {
    if (mMediaPlayer != null) {
      switchStateMediaPlayer(!mMediaPlayer!!.isPlaying)
    }
  }

  private fun switchStateMediaPlayer(isPlay: Boolean) {
    if (mMediaPlayer != null) {
      if (mMediaPlayer!!.isPlaying && !isPlay) {
        mMediaPlayer!!.pause()
        pauseScheduleWhenMusicStop()
      }

      if (!mMediaPlayer!!.isPlaying && isPlay) {
        mMediaPlayer!!.start()
        startScheduleWhenMusicRunning()
      }
    }
  }

  private fun updateMusicFromIntent(context: Context?, intent: Intent): Boolean {
    val bundle = intent.getBundleExtra(ACTION_MUSIC_DATA_BUNDLE)
    val data = bundle?.getSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC)

    try {
      if (data is Music) {
        mMediaPlayer!!.reset()

        Log.d("Binh", "Data Music: $data")
        if (data.isFromDevice) {
//          val assetFileDescriptor = assets.openFd(data.localHref!!)
//
//          mMediaPlayer!!.setDataSource(
//            assetFileDescriptor.fileDescriptor,
//            assetFileDescriptor.startOffset,
//            assetFileDescriptor.length
//          )

          mMediaPlayer!!.setDataSource(applicationContext, Uri.parse(data.localHref!!))
        } else {
          mMediaPlayer!!.setDataSource(data.href)
        }

        store.dispatch(MusicAction.MUSIC_ACTION_UPDATE_PREPARING_STATE(true))
        store.dispatch(MusicAction.MUSIC_ACTION_UPDATE_CURRENT_MUSIC(data))
        store.dispatch(GenreAction.GENRE_ACTION_ADD_MUSIC_INTO_PLAYLIST(data))

        if(!data.isFromDevice){
          MusicSharePreference.updateRecentlyPlayedMusic(data)
        }

        return true
      }

    } catch (err: Error) {
      context?.apply {
        Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
      }
    }
    return false
  }

  private fun initializeMediaPlayer() {
    mMediaPlayer = MediaPlayer()
    mMediaPlayer!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
    mMediaPlayer!!.setOnPreparedListener(this)
    mMediaPlayer!!.setOnErrorListener(this)

    wifiLock =
      (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager).createWifiLock(
        WIFI_MODE_FULL_HIGH_PERF,
        "com.thienbinh.halioplayer.wifilock"
      )

    wifiLock!!.acquire()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.d("Binh", "Intent: ${intent?.action}")

    when (intent?.action) {
      ACTION_MUSIC_INITIALIZE -> initializeMediaPlayer()
    }
    return START_REDELIVER_INTENT
  }

  override fun onBind(p0: Intent?): IBinder? {
    Log.d("Binh", "Service Bind ")
    return null
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d("Binh", "Service destroy")
    isRunning = false
    unregisterReceiver(mMusicBroadcastReceiver)

    mMediaPlayer!!.release()
    mMediaPlayer = null

    wifiLock?.release()
  }

  private fun startScheduleWhenMusicRunning() {
    if (mScheduledExecutorService == null) {
      mScheduledExecutorService = ScheduledThreadPoolExecutor(1)
    }

    pauseScheduleWhenMusicStop()

    store.dispatch(MusicAction.MUSIC_ACTION_UPDATE_PLAY_STATE(true))

    MusicInterfaceNotification.showNotification(applicationContext)

    scheduledFuture = mScheduledExecutorService!!.scheduleWithFixedDelay({
//      Log.d("Binh", "Time update: ${mMediaPlayer?.currentPosition}")
      store.dispatch(
        MusicAction.MUSIC_ACTION_UPDATE_CURRENT_POSITION(
          mMediaPlayer?.currentPosition ?: 0
        )
      )

      MusicInterfaceNotification.showNotification(applicationContext)
    }, TIME_DELAY, TIME_DELAY, TimeUnit.MILLISECONDS)
  }

  private fun pauseScheduleWhenMusicStop() {
    if (scheduledFuture != null) {
      scheduledFuture!!.cancel(true)
    }
    store.dispatch(MusicAction.MUSIC_ACTION_UPDATE_PLAY_STATE(false))
    MusicInterfaceNotification.showNotification(applicationContext)
  }

  override fun onPrepared(p0: MediaPlayer?) {
    if (isRunning && mMediaPlayer != null) {
      store.dispatch(MusicAction.MUSIC_ACTION_UPDATE_PREPARING_STATE(false))
      mMediaPlayer!!.start()
    }
    startScheduleWhenMusicRunning()
  }

  override fun onError(mediaPlayer: MediaPlayer?, p1: Int, p2: Int): Boolean {
    Log.d("Binh", "Error music play!!!")
    return false
  }

}


