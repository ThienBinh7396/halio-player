package com.thienbinh.halioplayer.store.reducer

import android.util.Log
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store.action.GenreAction
import com.thienbinh.halioplayer.store.state.GenreState
import com.thienbinh.halioplayer.store.state.RootState
import org.rekotlin.Action

fun genreReducer(action: Action, state: GenreState?): GenreState {
  var genreState = state ?: GenreState()

  when (action) {
    is GenreAction.GENRE_ACTION_UPDATE_LIST -> {
      genreState = genreState.copy(
        genres = action.list
      )
    }

    is GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST -> {
      genreState = genreState.copy(
        recentlyPlayed = action.list
      )
    }

    is GenreAction.GENRE_ACTION_UPDATE_ALBUMS -> {
      Log.d("Binh", "GENRE_ACTION_UPDATE_ALBUMS")

      genreState = genreState.copy(
        albums = action.list
      )
    }

    is GenreAction.GENRE_ACTION_UPDATE_LIST_FROM_DEVICE -> {
      genreState = genreState.copy(
        fromDeviceMusic = Music.deepCloneMusicList(action.list)
      )
    }

    is GenreAction.GENRE_ACTION_UPDATE_IS_LOADING_MUSIC_FROM_DEVICE -> {
      genreState = genreState.copy(
        isLoadingMusicFromDevice = action.isLoading
      )
    }

    is GenreAction.GENRE_ACTION_UPDATE_PLAYLIST -> {
      genreState = genreState.copy(
        playlists = action.list
      )
    }
  }

  return genreState
}