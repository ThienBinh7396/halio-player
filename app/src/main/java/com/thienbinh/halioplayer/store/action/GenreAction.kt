package com.thienbinh.halioplayer.store.action

import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import org.rekotlin.Action

sealed class GenreAction: Action {
  class GENRE_ACTION_UPDATE_LIST(var list: MutableList<Genre>): Action

  class GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(var list: MutableList<Music>): Action

  class GENRE_ACTION_UPDATE_ALBUMS(var list: MutableList<Album>): Action

  class GENRE_ACTION_UPDATE_PLAYLISTS(var list: MutableList<Album>): Action

  class GENRE_ACTION_UPDATE_LIST_FROM_DEVICE(var list: MutableList<Music>): Action
}