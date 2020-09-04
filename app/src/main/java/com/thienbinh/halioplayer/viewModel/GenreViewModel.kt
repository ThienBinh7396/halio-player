package com.thienbinh.halioplayer.viewModel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.Gson
import com.thienbinh.halioplayer.BR
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.state.GenreState
import org.rekotlin.StoreSubscriber

class GenreViewModel : BaseObservable(), StoreSubscriber<GenreState> {
  private var mGenres: MutableList<Genre>? = null

  private var mRecentlyPlayedList: MutableList<Music>? = null

  init {
    store.state.genreState.apply {
      mGenres = genres

      mRecentlyPlayedList = recentlyPlayed
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
  fun getRecentlyPlayed() = mRecentlyPlayedList

  fun getGenreById(id: Int): Genre {
    return mGenres?.find { it.id == id } ?: Genre.getInstance()[0]
  }

  override fun newState(state: GenreState) {
    if (!Genre.checkListAreTheSame(mGenres!!, state.genres)) {
      Log.d("Binh", "Update new genres")
      mGenres = state.genres
      notifyPropertyChanged(BR.genreViewModel)
      notifyPropertyChanged(BR.genres)
    }

    if (!Music.checkMusicListAreTheSame(mRecentlyPlayedList!!, state.recentlyPlayed)) {
      Log.d("Binh", "Update new recently played")
      mRecentlyPlayedList = state.recentlyPlayed

      notifyPropertyChanged(BR.recentlyPlayed)
    }
  }
}