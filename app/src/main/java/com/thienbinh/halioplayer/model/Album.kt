package com.thienbinh.halioplayer.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import java.io.Serializable

class Album(
  @SerializedName("id")
  var id: Int,
  @SerializedName("title")
  var title: String,
  @SerializedName("singer")
  var singer: String,
  @SerializedName("thumbnail")
  var thumbnail: String,
  @SerializedName("musics")
  var musics: MutableList<Music> = mutableListOf()
) : Serializable {
  companion object {
    private var gson = Gson()
    private var instance: MutableList<Album>? = null

    private var simpleAlbum = Album(
      0,
      "This Is Alan Walker",
      "Alan Walker",
      "https://i.scdn.co/image/ab67706f0000000351f5801c7cfb289e9e6fe7b9"
    )

    fun createInstance() {
      if (instance == null) {
        instance = mutableListOf()

        instance!!.apply {
          add(simpleAlbum)

          add(
            Album(
              size,
              "EDM Hits 2020",
              "Hallio Player",
              "https://i.scdn.co/image/ab67706c0000bebb17509f65aed1215c12cee1d0"
            )
          )

          add(
            Album(
              size,
              "Acoustic Hits",
              "Hallio Player",
              "https://i.scdn.co/image/ab67706f000000035ec18f4cc2ba9e3c5e287cd9"
            )
          )
        }
      }
    }

    fun getInstance(): MutableList<Album> {
      createInstance()

      return instance!!
    }

    fun getAlbumById(id: Int) = instance?.find { it.id == id } ?: simpleAlbum

    fun checkInstanceIsNull() = instance == null

    fun mapMusicToAlbum() {
      if (Music.checkInstanceIsNull()) {
        Music.initializeList()
      }
      if (checkInstanceIsNull()) {
        createInstance()
      }

      instance!!.forEach {
        it.musics =
          Music.getInstance()
            .filter { music -> music.albums.indexOfFirst { _album -> _album.id == it.id } > -1 }
            .toMutableList()

        Log.d("Binh", "Album: ${it.id} ${it.title} ${Gson().toJson(it.musics)}")
      }

      store.dispatch(GenreAction.GENRE_ACTION_UPDATE_ALBUMS(instance!!))
    }

    fun checkListAreTheSame(listOne: MutableList<Album>, listSecond: MutableList<Album>): Boolean {
      if (listOne.size != listSecond.size) return false

      var check = true

      listOne.forEachIndexed { index, album ->
        if (album.id != listSecond[index].id || !Music.checkMusicListAreTheSame(album.musics, listSecond[index].musics)) {
          check = false

          return@forEachIndexed
        }
      }

      return check
    }

    fun deepCloneAlbumList(list: MutableList<Album>): MutableList<Album> {
      return gson.fromJson(gson.toJson(list), Array<Album>::class.java).toMutableList()
    }
  }
}