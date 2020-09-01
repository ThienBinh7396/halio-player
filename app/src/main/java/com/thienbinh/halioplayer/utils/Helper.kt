package com.thienbinh.halioplayer.utils

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Helper {
  companion object {
    private val simpleDataFormatMusicDuration = SimpleDateFormat("mm:ss", Locale.US)

    @JvmStatic
    fun formatMusicDuration(milliSec: Long): String {
      return simpleDataFormatMusicDuration.format(Date(milliSec))
    }

    fun adjustAlpha(color: Int, factor: Float): Int {
      val alpha = (Color.alpha(color) * factor).roundToInt()
      val red: Int = Color.red(color)
      val green: Int = Color.green(color)
      val blue: Int = Color.blue(color)
      return Color.argb(alpha, red, green, blue)
    }
  }
}