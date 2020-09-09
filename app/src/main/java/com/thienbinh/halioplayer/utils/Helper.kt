package com.thienbinh.halioplayer.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import com.thienbinh.halioplayer.model.Lyric
import com.thienbinh.halioplayer.model.Music
import org.joda.time.format.DateTimeFormat.forPattern
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class Helper {
  companion object {
    private val simpleDateFormatMusicDuration = SimpleDateFormat("mm:ss", Locale.US)
    private val simpleDateFormatMusicTime = SimpleDateFormat("mm:ss.SS", Locale.US)
    private val jodaTimeFormatMusicTime = forPattern("mm:ss.SS")

    private val lineLyricPattern =
      "\\[(.*?)](.*)".toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE))

    @JvmStatic
    fun formatMusicDuration(milliSec: Long): String {
      return simpleDateFormatMusicDuration.format(Date(milliSec))
    }

    @JvmStatic
    fun formatMusicDuration(milliSec: Int): String {
      return simpleDateFormatMusicDuration.format(Date(milliSec.toLong()))
    }

    @JvmStatic
    fun convertTimeStringToMilliSec(time: String): Int {
      val date = jodaTimeFormatMusicTime.parseDateTime(time)
      return date.millisOfDay
    }

    @JvmStatic
    fun convertLineFromLrcFileToLyric(line: String): Lyric {
      val matchResult = lineLyricPattern.find(line)
      val (time, content) = matchResult!!.destructured

      return Lyric(time, content)
    }

    fun adjustAlpha(color: Int, factor: Float): Int {
      val alpha = (Color.alpha(color) * factor).roundToInt()
      val red: Int = Color.red(color)
      val green: Int = Color.green(color)
      val blue: Int = Color.blue(color)
      return Color.argb(alpha, red, green, blue)
    }

    @JvmStatic
    fun checkListIsEmpty(list: MutableList<Music>?) = list == null || list.size == 0


    @JvmStatic
    fun spToPx(sp: Float, context: Context): Int {
      return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
      )
        .toInt()
    }
  }
}