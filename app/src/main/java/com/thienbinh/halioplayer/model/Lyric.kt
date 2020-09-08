package com.thienbinh.halioplayer.model

import com.google.gson.Gson
import com.thienbinh.halioplayer.utils.Helper

class Lyric(
  private var timeString: String,
  var content: String,
  var isActive: Boolean = false,
  var position: Int = 0
) {
  companion object {
    private val gson = Gson()

    fun deepCloneLyricList(list: MutableList<Lyric>): MutableList<Lyric> {
      return gson.fromJson(gson.toJson(list), Array<Lyric>::class.java).toMutableList()
    }


    fun deepCloneLyric(lyric: Lyric) = gson.fromJson(gson.toJson(lyric), Lyric::class.java)
  }

  var time: Int = 0

  fun getTimeWithoutConvert() = timeString

  init {
    this.time = Helper.convertTimeStringToMilliSec(timeString)
  }

}