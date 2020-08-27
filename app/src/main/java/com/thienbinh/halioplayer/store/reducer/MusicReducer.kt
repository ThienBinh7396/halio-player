package com.thienbinh.halioplayer.store.reducer

import com.thienbinh.halioplayer.store.state.MusicState
import org.rekotlin.Action

fun musicReducer(action: Action, musicState: MusicState?): MusicState {
  val _musicState = musicState?: MusicState()

  return _musicState
}