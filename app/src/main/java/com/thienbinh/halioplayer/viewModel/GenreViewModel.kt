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

  private var mPlaylist: MutableList<Music>? = null

  private var mAlbums: MutableList<Album>? = null

  private var mMusicFromDevice: MutableList<Music>? = null

  private var mIsLoadingMusicFromDevice: Boolean = false

  init {
    store.state.genreState.apply {
      mGenres = genres

      mRecentlyPlayedList = recentlyPlayed

      mPlaylist = playlists

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

  @Bindable
  fun getPlaylist() = mPlaylist

  fun getGenreById(id: Int): Genre {
    return mGenres?.find { it.id == id } ?: Genre.getInstance()[0]
  }

  @Bindable
  fun getMusicFromDevice() = mMusicFromDevice

  fun getMusicFromDeviceWithout(
    music: Music?
  ): MutableList<Music> {
    if (music == null) return mMusicFromDevice!!

    val musicListFind = mMusicFromDevice?.filter { it.id != music.id }

    return musicListFind?.toMutableList() ?: mutableListOf()
  }

  @Bindable
  fun getIsLoadingMusicFromDevice() = mIsLoadingMusicFromDevice

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

    if (!Music.checkMusicListAreTheSame(mPlaylist!!, state.playlists)) {
      Log.d("Binh", "Update new recently played")
      mPlaylist = Music.deepCloneMusicList(state.playlists)

      notifyPropertyChanged(BR.playlist)
    }

    if (!Album.checkListAreTheSame(mAlbums!!, state.albums)) {
      mAlbums = state.albums

      Log.d("Binh", "Albums update ${Gson().toJson(mAlbums!!)}")

      notifyPropertyChanged(BR.albums)
    }

    if (!Music.checkMusicListAreTheSame(mMusicFromDevice!!, state.fromDeviceMusic)) {
      mMusicFromDevice = state.fromDeviceMusic

      notifyPropertyChanged(BR.musicFromDevice)

      Log.d("Binh", "IS LOAIDNG : ${mMusicFromDevice?.size}")
    }

    if (mIsLoadingMusicFromDevice != state.isLoadingMusicFromDevice) {
      mIsLoadingMusicFromDevice = state.isLoadingMusicFromDevice

      notifyPropertyChanged(BR.isLoadingMusicFromDevice)
    }
  }

  companion object {
    @JvmStatic
    fun getMusicFromDeviceWithout(
      music: Music?,
      list: MutableList<Music> = mutableListOf()
    ): MutableList<Music> {
      if (music == null) return list

      return list.filter { it.id != music.id }.toMutableList()
    }

    @JvmStatic
    fun getPlaylistWithout(
      music: Music?,
      list: MutableList<Music>? = mutableListOf()
    ): MutableList<Music> {
      if (music == null) return list ?: mutableListOf()

      val getList = list?.filter { it.id != music.id }?.toMutableList()

      return getList ?: mutableListOf()
    }
  }
}