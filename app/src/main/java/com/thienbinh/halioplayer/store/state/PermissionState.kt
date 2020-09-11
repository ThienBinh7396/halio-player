package com.thienbinh.halioplayer.store.state

import org.rekotlin.StateType

data class PermissionState(
  var readExternalStoragePermissionState: Boolean = false,
  var isFirstLoadMusicFromDevice: Boolean = false
) : StateType