package com.thienbinh.halioplayer.store.state

import com.thienbinh.halioplayer.model.Music
import org.rekotlin.StateType

data class MusicState(
  var isPlaying: Boolean = false,
  var isPreparing: Boolean = false,
  var currentMusic: Music? = null,
  var currentPosition: Int = 0
) : StateType