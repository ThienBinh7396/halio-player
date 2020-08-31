package com.thienbinh.halioplayer.viewModel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.thienbinh.halioplayer.BR
import com.thienbinh.halioplayer.customInterface.IMusicControlEventListener
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.state.MusicState
import com.thienbinh.halioplayer.utils.Helper
import org.rekotlin.StoreSubscriber

class MusicStoreViewModel(private var musicControlEventListener: IMusicControlEventListener) :
  BaseObservable(),
  StoreSubscriber<MusicState> {
  private var mMusic: Music? = null

  private var mIsPlaying = false

  private var mCurrentPosition: Int = 0

  init {
    store.subscribe(this) {
      it.select {
        it.musicState
      }
    }
  }

  @get:Bindable
  val music
    get() = mMusic

  @get:Bindable
  val currentPosition
    get() = mCurrentPosition

  @get:Bindable
  val isPlaying
    get() = mIsPlaying

  @get:Bindable
  val eventListener
    get() = musicControlEventListener

  @get:Bindable
  val currentProgressText
    get() = if (mMusic != null) mCurrentPosition / 10 / mMusic!!.duration else 0

  override fun newState(state: MusicState) {
    state.apply {
      mMusic = currentMusic
      mCurrentPosition = currentPosition
      mIsPlaying = isPlaying

      notifyPropertyChanged(BR.currentProgressText)
      notifyPropertyChanged(BR.music)
      notifyPropertyChanged(BR.playing)
    }
  }
}