package com.thienbinh.halioplayer.store.reducer

import com.thienbinh.halioplayer.store.action.PermissionAction
import com.thienbinh.halioplayer.store.state.PermissionState
import org.rekotlin.Action

fun permissionReducer(action: Action, permissionState: PermissionState?): PermissionState {
  var _permissionState = permissionState ?: PermissionState()

  when (action) {
    is PermissionAction.PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION -> {
      _permissionState = _permissionState.copy(
        readExternalStoragePermissionState = action.isGranted
      )
    }

    is PermissionAction.PERMISSION_ACTION_UPDATE_IS_FIRST_LOAD_MUSIC -> {
      _permissionState = _permissionState.copy(
        isFirstLoadMusicFromDevice = action.isFirstLoadMusicFromDevice
      )
    }
  }

  return _permissionState
}