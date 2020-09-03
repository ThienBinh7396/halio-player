package com.thienbinh.halioplayer.model

import android.util.Log
import java.io.Serializable

class Genre(
  var id: Int,
  var title: String,
  var thumbnail: String?,
  var musicList: MutableList<Music> = mutableListOf()
) : Serializable {
  companion object {
    private var instance: MutableList<Genre>? = null

    fun checkInstanceIsNull() = instance == null

    fun createInstance() {
      if (instance == null) {
        instance = mutableListOf(
          Genre(-1, "Recently Played", null),
          Genre(0, "Made for you", null),
          Genre(1, "Tâm trạng", "https://i.scdn.co/image/ab67706f00000002935d747bed47e6ae6e1bf0b8"),
          Genre(
            2,
            "Life sucks",
            "https://i.scdn.co/image/ab67706f00000003f5e3bf0413ec122f118e5f08"
          ),
          Genre(
            3,
            "Peaceful Piano",
            "https://i.scdn.co/image/ab67706f00000003ca5a7517156021292e5663a6"
          ),
          Genre(4, "Sleep", "https://i.scdn.co/image/ab67706f00000003b70e0223f544b1faa2e95ed0"),
          Genre(
            5,
            "Piano in the Background",
            "https://i.scdn.co/image/ab67706f00000003a461b85872ea87bb0de00128"
          )
        )
      }
    }

    fun mapMusicToGenre() {
      if (Music.checkInstanceIsNull() || checkInstanceIsNull()) return

      instance!!.forEach {
        it.musicList.addAll(Music.getInstance().filter { music -> music.genre.contains(it) })

        Log.d("Binh", "Music : ${it.title} ${it.musicList}")
      }
    }

    fun getInstance(): MutableList<Genre> {
      createInstance()
      return instance!!
    }

    fun getGenreById(id: Int): Genre? = getInstance().find { genre -> genre.id == id }
  }
}