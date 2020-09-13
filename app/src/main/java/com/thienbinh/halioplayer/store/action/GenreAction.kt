package com.thienbinh.halioplayer.store.action

import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import org.rekotlin.Action

sealed class GenreAction: Action {
  class GENRE_ACTION_UPDATE_LIST(var list: MutableList<Genre>): Action

  class GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(var list: MutableList<Music>): Action

  class GENRE_ACTION_UPDATE_ALBUMS(var list: MutableList<Album>): Action

  class GENRE_ACTION_ADD_MUSIC_INTO_PLAYLIST(var music: Music): Action

  class GENRE_ACTION_UPDATE_PLAYLIST(var list: MutableList<Music>): Action

  class GENRE_ACTION_UPDATE_LIST_FROM_DEVICE(var list: MutableList<Music>): Action

  class GENRE_ACTION_UPDATE_IS_LOADING_MUSIC_FROM_DEVICE(var isLoading: Boolean): Action
}