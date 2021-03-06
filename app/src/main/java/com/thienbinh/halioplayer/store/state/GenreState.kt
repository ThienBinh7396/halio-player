package com.thienbinh.halioplayer.store.state

import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import org.rekotlin.StateType

data class GenreState(
  val genres: MutableList<Genre> = mutableListOf(),
  val recentlyPlayed: MutableList<Music> = mutableListOf(),
  val playlists: MutableList<Music> = mutableListOf(),
  val fromDeviceMusic: MutableList<Music> = mutableListOf(),
  var isLoadingMusicFromDevice: Boolean = true,
  val albums: MutableList<Album> = mutableListOf()
) : StateType