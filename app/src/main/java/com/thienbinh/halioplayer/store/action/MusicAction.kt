package com.thienbinh.halioplayer.store.action

import org.rekotlin.Action

sealed class MusicAction : Action {
  class MUSIC_ACTION_UPDATE_STATE() : Action
}