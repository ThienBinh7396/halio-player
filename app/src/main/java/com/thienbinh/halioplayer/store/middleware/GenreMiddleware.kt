package com.thienbinh.halioplayer.store.middleware

import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import com.thienbinh.halioplayer.store.state.RootState
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware

internal val genreMiddleware: Middleware<RootState> = { dispatch, _ ->
  { next ->
    { action ->
      run {
        when (action) {
          is GenreAction.GENRE_ACTION_ADD_MUSIC_INTO_PLAYLIST -> {
            addMusicIntoPlaylist(action.music)
          }
        }

        next(action)
      }
    }
  }
}

fun addMusicIntoPlaylist(music: Music) {
  store.state.genreState.apply {
    val getIndexInPlaylist = playlists.indexOfFirst { it.id == music.id }

    if (getIndexInPlaylist > -1){
      playlists.removeAt(getIndexInPlaylist)
    }

    playlists.add(0, music)

    store.dispatch(GenreAction.GENRE_ACTION_UPDATE_PLAYLIST(playlists))
  }
}