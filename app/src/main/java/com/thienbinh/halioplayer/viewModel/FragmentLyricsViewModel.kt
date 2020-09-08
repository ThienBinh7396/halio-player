package com.thienbinh.halioplayer.viewModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.thienbinh.halioplayer.BR
import com.thienbinh.halioplayer.model.Lyric

class FragmentLyricsViewModel: BaseObservable() {
  private var mLyrics = mutableListOf<Lyric>()

  @Bindable
  fun getLyrics() = mLyrics

  fun updateLyrics(newList: MutableList<Lyric>){
    mLyrics = Lyric.deepCloneLyricList(newList)

    notifyPropertyChanged(BR.lyrics)
  }
}