package com.thienbinh.halioplayer.store.action

import com.thienbinh.halioplayer.model.Music
import org.rekotlin.Action

sealed class MusicAction : Action {
  class MUSIC_ACTION_UPDATE_CURRENT_POSITION(var position: Int) : Action
  class MUSIC_ACTION_UPDATE_CURRENT_MUSIC(var music: Music) : Action
  class MUSIC_ACTION_UPDATE_PLAY_STATE(var isPlaying: Boolean) : Action
  class MUSIC_ACTION_UPDATE_PREPARING_STATE(var isPreparing: Boolean) : Action
}