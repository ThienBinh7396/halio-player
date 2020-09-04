package com.thienbinh.halioplayer.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction

class FirstActionInitializeData {
  companion object {
    fun initialize(context: Context) {
      Genre.createInstance()
      Music.initializeList()

      if (Genre.getGenreById(0)!!.musicList.size == 0){
        Genre.mapMusicToGenre()

        Log.d("Binh", "Gson: ${Gson().toJson(MusicSharePreference.getRecentlyPlayedMusic())}")

        store.dispatch(GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(MusicSharePreference.getRecentlyPlayedMusic()))
      }
    }
  }
}