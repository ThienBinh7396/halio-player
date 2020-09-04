package com.thienbinh.halioplayer.sharePreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import java.lang.Error

class MusicSharePreference(val context: Context) {
  companion object {
    private var mContext: Context? = null

    private val RECENTLY_PLAYED_MUSIC_KEY = "RECENTLY_PLAYED_MUSIC_KEY"

    private val gson = Gson()

    fun updateContext(context: Context) {
      mContext = context
    }

    private fun getMusicSharedPreference(): SharedPreferences? {
      try {
        return mContext?.getSharedPreferences("music", MODE_PRIVATE)
      } catch (err: Error) {
        Log.d("Binh", "Music shared preference: ${err.message}")
      }
      return null
    }

    private fun getValueOfMusicSharedPreferenceByKey(key: String): String? {
      return getMusicSharedPreference()?.getString(key, null)
    }

    fun getRecentlyPlayedMusic() = gson.fromJson(
      getValueOfMusicSharedPreferenceByKey(RECENTLY_PLAYED_MUSIC_KEY) ?: "[]",
      Array<Music>::class.java
    ).toMutableList()

    fun updateRecentlyPlayedMusic(music: Music) {
      val musicList = getRecentlyPlayedMusic()

      var mMusic = music

      val index = musicList.indexOfFirst { it.id == mMusic.id }

      if (index == 0) return

      if (index > -1) {
        musicList.removeAt(index)

        mMusic = musicList[index]

        mMusic.count_play++
      }

      musicList.add(0, mMusic)


      Handler(Looper.getMainLooper()).postDelayed({
        getMusicSharedPreference()?.apply {
          edit().putString(RECENTLY_PLAYED_MUSIC_KEY, gson.toJson(musicList)).apply()
        }

        store.dispatch(GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(musicList))
      }, 10)

    }
  }
}