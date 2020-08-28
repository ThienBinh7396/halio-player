package com.thienbinh.halioplayer

import android.app.Application
import com.thienbinh.halioplayer.store.reducer.rootReducer
import org.rekotlin.Store

val store = Store(
  reducer = ::rootReducer,
  state = null,
  automaticallySkipRepeats = false
)

class MainApplication : Application()