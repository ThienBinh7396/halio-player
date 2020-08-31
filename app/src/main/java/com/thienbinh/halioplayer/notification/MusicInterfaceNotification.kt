package com.thienbinh.halioplayer.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.IdRes
import androidx.core.app.NotificationCompat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.NotificationTarget
import com.thienbinh.halioplayer.GlideApp
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.utils.RotateImageTransformation

class MusicInterfaceNotification {
  companion object {
    val MUSIC_INTERFACE_NOTIFICATION_ID = 1996

    val MUSIC_INTERFACE_NOTIFICATION_CHANNEL_ID =
      "com.thienbinh.halioplayer.music.MUSIC_INTERFACE_NOTIFICATION_CHANNEL_ID"
    val MUSIC_INTERFACE_NOTIFICATION_CHANNEL_NAME =
      "com.thienbinh.halioplayer.music.MUSIC_INTERFACE_NOTIFICATION_CHANNEL_NAME"
    val MUSIC_INTERFACE_NOTIFICATION_CHANNEL_DESCRIPTION =
      "com.thienbinh.halioplayer.music.MUSIC_INTERFACE_NOTIFICATION_CHANNEL_DESCRIPTION"
    val MUSIC_INTERFACE_NOTIFICATION_CHANNEL_INPORTANCE =
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        NotificationManager.IMPORTANCE_MAX
      } else {
        Notification.PRIORITY_MAX
      }

    private fun onButtonNotificationClick(@IdRes id: Int) {

    }

    private fun generateExpandLayout(context: Context): RemoteViews {
      val notificationLayout =
        RemoteViews(context.packageName, R.layout.notification_interface_music_large)

      store.state.musicState.apply {
        Log.d(
          "Binh",
          "Music: $currentMusic $currentPosition ${(currentPosition / 10) / currentMusic!!.duration}"
        )

        notificationLayout.setTextViewText(R.id.tvTitle, currentMusic?.title)
        notificationLayout.setTextViewText(R.id.tvSinger, currentMusic?.singer)
        notificationLayout.setImageViewResource(
          R.id.btnToggleState,
          if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
        )

        notificationLayout.setProgressBar(
          R.id.prbCurrentDuration,
          100,
          if (currentMusic?.duration != null) currentPosition / 10 / currentMusic!!.duration else 0,
          false
        )
      }

      return notificationLayout
    }

    fun showNotification(context: Context) {
      Log.d("Binh", "Show notification")

      val notificationExpandLayout = generateExpandLayout(context)

      val notification =
        NotificationCompat.Builder(context, MUSIC_INTERFACE_NOTIFICATION_CHANNEL_ID)
          .setVisibility(NotificationCompat.VISIBILITY_SECRET)
          .setSmallIcon(R.drawable.logo_sm)
          .setCustomContentView(notificationExpandLayout)
          .setPriority(MUSIC_INTERFACE_NOTIFICATION_CHANNEL_INPORTANCE)
          .build()

      notification.flags = Notification.FLAG_ONGOING_EVENT or Notification.FLAG_NO_CLEAR

      (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
        MUSIC_INTERFACE_NOTIFICATION_ID,
        notification
      )

      val notificationTargetExpandedThumbnailImage = NotificationTarget(
        context,
        R.id.imvThumbnail,
        notificationExpandLayout,
        notification,
        MUSIC_INTERFACE_NOTIFICATION_ID
      )

      Handler(Looper.getMainLooper()).postDelayed({
        GlideApp.with(context.applicationContext)
          .asBitmap()
          .load(store.state.musicState.currentMusic?.thumbnail)
          .circleCrop()
          .placeholder(R.drawable.logo_sm)
          .into(notificationTargetExpandedThumbnailImage)
      }, 10)
    }
  }
}