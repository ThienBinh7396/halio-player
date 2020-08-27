package com.thienbinh.halioplayer.store.state

import org.rekotlin.StateType

class MusicState(
  totalDuration: Int = 0,
  currentPosition: Int = 0
) : StateType