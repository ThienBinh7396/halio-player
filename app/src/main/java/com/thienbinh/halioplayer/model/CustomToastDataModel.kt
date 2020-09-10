package com.thienbinh.halioplayer.model

import com.thienbinh.halioplayer.customInterface.ICustomSnackbarEventListener

data class CustomToastDataModel(
  var message: String,
  var primaryText: String,
  var eventListener: ICustomSnackbarEventListener
)