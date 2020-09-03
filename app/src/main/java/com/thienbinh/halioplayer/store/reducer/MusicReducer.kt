package com.thienbinh.halioplayer.store.reducer

import com.thienbinh.halioplayer.store.action.MusicAction
import com.thienbinh.halioplayer.store.state.MusicState
import org.rekotlin.Action

fun musicReducer(action: Action, musicState: MusicState?): MusicState {
  var mMusicState = musicState ?: MusicState()

  when (action) {
    is MusicAction.MUSIC_ACTION_UPDATE_CURRENT_POSITION -> {
      mMusicState = mMusicState.copy(
        currentPosition = action.position
      )
    }

    is MusicAction.MUSIC_ACTION_UPDATE_PLAY_STATE -> {
      mMusicState = mMusicState.copy(
        isPlaying = action.isPlaying
      )
    }

    is MusicAction.MUSIC_ACTION_UPDATE_PREPARING_STATE -> {
      mMusicState = mMusicState.copy(
        isPreparing = action.isPreparing
      )
    }

    is MusicAction.MUSIC_ACTION_UPDATE_CURRENT_MUSIC -> {
      mMusicState = mMusicState.copy(
        currentMusic = action.music
      )
    }
  }

  return mMusicState
}