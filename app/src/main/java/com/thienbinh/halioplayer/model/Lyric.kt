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

    fun deepCloneGenreList(list: MutableList<Lyric>): MutableList<Lyric> {
      return gson.fromJson(gson.toJson(list), Array<Lyric>::class.java).toMutableList()
    }
  }

  var time: Int = 0

  fun getTimeWithoutConvert() = timeString

  init {
    this.time = Helper.convertTimeStringToMilliSec(timeString)
  }

}