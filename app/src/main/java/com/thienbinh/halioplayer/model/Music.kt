package com.thienbinh.halioplayer.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Music(
  @SerializedName("id")
  var id: Int,
  @SerializedName("title")
  var title: String,
  @SerializedName("thumbnail")
  var thumbnail: String,
  @SerializedName("singer")
  var singer: String,
  @SerializedName("href")
  var href: String,
  @SerializedName("duration")
  var duration: Int,
  @SerializedName("genre")
  var genre: MutableList<Genre>
) : Serializable {
  companion object {
    private var instance: MutableList<Music>? = null

    fun getInstance(): MutableList<Music> {
      if (instance == null) {
        instance = mutableListOf()
        initializeList()
      }

      return instance!!
    }

    fun getMusicById(id: Int): Music? {
      if (instance == null) getInstance()

      return instance!!.find { it.id == id }
    }

    private fun initializeList() {
      if (instance != null) {
        /*List mood music*/

        instance!!.add(
          Music(
            1,
            "Let me down slowly",
            "https://i.scdn.co/image/ab67616d00001e02459d675aa0b6f3b211357370",
            "Alec Benjamin",
            "let_me_down_slowly.mp3",
            169,
            mutableListOf(
              Genre.getGenreById(1)!!
            )
          )
        )
      }
    }
  }
}