package com.thienbinh.halioplayer.viewModel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import com.thienbinh.halioplayer.BR
import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.state.GenreState
import org.rekotlin.StoreSubscriber

class GenreViewModel : BaseObservable(), StoreSubscriber<GenreState> {
  private var mGenres: MutableList<Genre>? = null

  private var mRecentlyPlayedList: MutableList<Music>? = null

  private var mAlbums: MutableList<Album>? = null

  private var mMusicFromDevice: MutableList<Music>? = null

  init {
    store.state.genreState.apply {
      mGenres = genres

      mRecentlyPlayedList = recentlyPlayed

      mAlbums = albums

      mMusicFromDevice = fromDeviceMusic
    }

    store.subscribe(this) {
      it.select {
        it.genreState
      }
    }
  }

  @Bindable
  var genres = mGenres ?: Genre.getInstance()

  @Bindable
  fun getAlbums() = mAlbums ?: Album.getInstance()

  fun getAlbumById(id: Int): Album {
    return mAlbums?.find { it.id == id } ?: Album.getInstance()[0]
  }

  @Bindable
  fun getRecentlyPlayed() = mRecentlyPlayedList

  fun getGenreById(id: Int): Genre {
    return mGenres?.find { it.id == id } ?: Genre.getInstance()[0]
  }

  @Bindable
  fun getMusicFromDevice() = mMusicFromDevice

  fun getMusicFromDeviceWithout(music: Music?) = if (music == null) mMusicFromDevice else (
      mMusicFromDevice?.filter { it.id != music.id }
        ?: mutableListOf()
      )


  override fun newState(state: GenreState) {
    if (!Genre.checkListAreTheSame(mGenres!!, state.genres)) {
      Log.d("Binh", "Update new genres")
      mGenres = Genre.deepCloneGenreList(state.genres)
      notifyPropertyChanged(BR.genreViewModel)
      notifyPropertyChanged(BR.genres)
    }

    if (!Music.checkMusicListAreTheSame(mRecentlyPlayedList!!, state.recentlyPlayed)) {
      Log.d("Binh", "Update new recently played")
      mRecentlyPlayedList = state.recentlyPlayed

      notifyPropertyChanged(BR.recentlyPlayed)
    }

    if (!Album.checkListAreTheSame(mAlbums!!, state.albums)) {
      mAlbums = state.albums

      Log.d("Binh", "Albums update ${Gson().toJson(mAlbums!!)}")

      notifyPropertyChanged(BR.albums)
    }

    if (!Music.checkMusicListAreTheSame(mMusicFromDevice!!, state.fromDeviceMusic)) {
      mMusicFromDevice = state.fromDeviceMusic

      notifyPropertyChanged(BR.musicFromDevice)
    }
  }
}