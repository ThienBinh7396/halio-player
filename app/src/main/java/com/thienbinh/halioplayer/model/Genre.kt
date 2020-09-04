package com.thienbinh.halioplayer.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import java.io.Serializable

class Genre(
  @SerializedName("id")
  var id: Int,
  @SerializedName("title")
  var title: String,
  @SerializedName("thumbnail")
  var thumbnail: String?,
  @SerializedName("musicList")
  var musicList: MutableList<Music> = mutableListOf()
) : Serializable {
  companion object {
    private var instance: MutableList<Genre>? = null

    private val gson = Gson()

    fun checkInstanceIsNull() = instance == null

    fun createInstance() {
      if (instance == null) {
        instance = mutableListOf(
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
      if (Music.checkInstanceIsNull()) {
        Music.initializeList()
      }
      if (checkInstanceIsNull()) {
        createInstance()
      }

      instance!!.forEach {
        it.musicList =
          Music.getInstance()
            .filter { music -> music.genre.indexOfFirst { _genre -> _genre.id == it.id } > -1 }
            .toMutableList()

        Log.d("Binh", "Music : ${it.title} ${it.musicList}")
      }


      store.dispatch(GenreAction.GENRE_ACTION_UPDATE_LIST(instance!!))
    }

    fun getInstance(): MutableList<Genre> {
      createInstance()
      return instance!!
    }

    fun getGenreById(id: Int): Genre? = getInstance().find { genre -> genre.id == id }

    fun checkListAreTheSame(listOne: MutableList<Genre>, listSecond: MutableList<Genre>): Boolean {
      if (listOne.size != listSecond.size) return false

      var check = true

      listOne.forEachIndexed { index, genre ->
        if (!Music.checkMusicListAreTheSame(genre.musicList, listSecond[index].musicList)) {
          check = false

          return@forEachIndexed
        }
      }

      return check
    }

    fun deepCloneGenreList(list: MutableList<Genre>): MutableList<Genre> {
      return gson.fromJson(gson.toJson(list), Array<Genre>::class.java).toMutableList()
    }
  }
}