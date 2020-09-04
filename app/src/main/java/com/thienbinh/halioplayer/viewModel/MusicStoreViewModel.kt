package com.thienbinh.halioplayer.viewModel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.thienbinh.halioplayer.BR
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.customInterface.IMainActivityEventListener
import com.thienbinh.halioplayer.customInterface.IMusicControlEventListener
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.state.MusicState
import com.thienbinh.halioplayer.utils.Helper
import org.rekotlin.StoreSubscriber

class MusicStoreViewModel(
  private var musicControlEventListener: IMusicControlEventListener? = null,
  private var mainActivityEventListener: IMainActivityEventListener? = null
) :
  BaseObservable(),
  StoreSubscriber<MusicState> {
  private var mMusic: Music? = null

  private var mIsPlaying = false

  private var mIsPreparing: Boolean = false

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

  @Bindable
  fun getMusicDuration() = if (mMusic?.duration != null) mMusic!!.duration * 1000 else 0

  @get:Bindable
  val currentPosition
    get() = mCurrentPosition

  @get:Bindable
  val isPlaying
    get() = mIsPlaying

  @get:Bindable
  val isPreparing
    get() = mIsPreparing

  @get:Bindable
  val eventListener
    get() = musicControlEventListener

  @get:Bindable
  val mainActivityListener
    get() = mainActivityEventListener

  @get:Bindable
  val currentProgressText
    get() = if (mMusic != null) mCurrentPosition / 10 / mMusic!!.duration else 0

  override fun newState(state: MusicState) {
    state.apply {
      if (currentMusic != null && (mMusic == null || mMusic!!.id != currentMusic?.id)) {
        mMusic = currentMusic
        notifyPropertyChanged(BR.musicDuration)
        notifyPropertyChanged(BR.music)
      }

      if (mCurrentPosition != currentPosition) {
        mCurrentPosition = currentPosition

        notifyPropertyChanged(BR.currentProgressText)
        notifyPropertyChanged(BR.currentPosition)
      }

      if (mIsPlaying != isPlaying) {
        mIsPlaying = isPlaying

        notifyPropertyChanged(BR.playing)
      }

      if (mIsPreparing != isPreparing) {
        mIsPreparing = isPreparing
        notifyPropertyChanged(BR.preparing)
      }
    }
  }
}