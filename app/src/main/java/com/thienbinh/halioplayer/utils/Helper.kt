package com.thienbinh.halioplayer.utils

import java.text.SimpleDateFormat
import java.util.*

class Helper {
  companion object {
    private val simpleDataFormatMusicDuration = SimpleDateFormat("mm:ss", Locale.US)

    fun formatMusicDuration(milliSec: Long): String {
      return simpleDataFormatMusicDuration.format(Date(milliSec))
    }
  }
}