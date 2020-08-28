package com.thienbinh.halioplayer.viewModel

import androidx.databinding.BaseObservable
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.utils.Helper

class NotificationInterfaceMusicViewModel(
  var music: Music,
  var isPlaying: Boolean,
  var position: Int = 0
) : BaseObservable() {

  fun getProgress(): Int {
    return position / 1000 / music.duration
  }

  fun getCurrentPositionText() = Helper.formatMusicDuration(position.toLong())
}