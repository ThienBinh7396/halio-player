package com.thienbinh.halioplayer

import android.app.Application
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.store.reducer.rootReducer
import com.thienbinh.halioplayer.ui.snackbar.CustomSnackbar
import org.rekotlin.Store

val store = Store(
  reducer = ::rootReducer,
  state = null
)

class MainApplication : Application(){
  override fun onCreate() {
    super.onCreate()

    MusicSharePreference.updateContext(applicationContext)
  }

  override fun onTerminate() {
    super.onTerminate()

    CustomSnackbar.updateActivity(null)
  }
}