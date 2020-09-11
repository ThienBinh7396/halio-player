package com.thienbinh.halioplayer.store.action

import org.rekotlin.Action

sealed class PermissionAction: Action {
  class PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION(var isGranted: Boolean): Action

  class PERMISSION_ACTION_UPDATE_IS_FIRST_LOAD_MUSIC(var isFirstLoadMusicFromDevice: Boolean): Action

}