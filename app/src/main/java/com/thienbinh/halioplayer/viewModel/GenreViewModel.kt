package com.thienbinh.halioplayer.viewModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.thienbinh.halioplayer.model.Genre

class GenreViewModel : BaseObservable() {
  @Bindable
  val genres = Genre.getInstance()

  fun getGenreById(id: Int): Genre {
    return genres.find { it.id == id } ?: genres[0]
  }
}