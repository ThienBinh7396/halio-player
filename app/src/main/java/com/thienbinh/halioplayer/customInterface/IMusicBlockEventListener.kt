package com.thienbinh.halioplayer.customInterface

import com.thienbinh.halioplayer.model.Music

interface IMusicBlockEventListener {
  fun onWidgetButtonClickListener(musicId: Int)

  fun onContainerClickListener(music: Music)
}