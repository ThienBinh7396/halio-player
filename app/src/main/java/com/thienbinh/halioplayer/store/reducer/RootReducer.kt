package com.thienbinh.halioplayer.store.reducer

import com.thienbinh.halioplayer.store.state.RootState
import org.rekotlin.Action

fun rootReducer(action: Action, rootState: RootState?): RootState =
  RootState(
    musicState = musicReducer(action, rootState?.musicState),
    genreState = genreReducer(action, rootState?.genreState),
    permissionState = permissionReducer(action, rootState?.permissionState)
  )