package com.thienbinh.halioplayer.sharePreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.google.gson.Gson
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EMusicListTypeSort
import com.thienbinh.halioplayer.customInterface.ICustomSnackbarEventListener
import com.thienbinh.halioplayer.model.CustomToastDataModel
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import com.thienbinh.halioplayer.ui.snackbar.CustomSnackbar
import java.lang.Error

class MusicSharePreference(val context: Context) {
  companion object {
    private var mContext: Context? = null

    private val RECENTLY_PLAYED_MUSIC_KEY = "RECENTLY_PLAYED_MUSIC_KEY"

    private val gson = Gson()

    private val handlerThread = HandlerThread("MusicSharePreference")

    init {
      handlerThread.start()
    }

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

    fun removeMusicWhere(id: Int) {
      val musicList = getRecentlyPlayedMusic()

      var musicIndex: Int = -1

      var music: Music? = null
      musicList.forEachIndexed { _index, _music ->
        run {
          if (_music.id == id) {
            music = _music
            musicIndex = _index

            return@forEachIndexed
          }
        }
      }

      if (musicIndex > -1) {
        musicList.removeAt(musicIndex)

        Handler(handlerThread.looper).postDelayed({
          handleUpdateRecentlyPlayedFromSharedPreference(musicList)

          mContext?.apply {
            CustomSnackbar.showSnackbar(CustomToastDataModel(
              "Delete music from recently played successful!",
              "Undo",
              object : ICustomSnackbarEventListener {
                override fun onPrimaryButtonClickListener() {
                  musicList.add(musicIndex, music!!)

                  handleUpdateRecentlyPlayedFromSharedPreference(musicList)
                }

                override fun onSecondaryButtonClickListener() {
                }
              }
            ))
          }
        }, 100)
      }
    }

    fun removeMusicFromPlaylistWhere(id: Int) {
      store.state.genreState.apply {
        val musicList = playlists

        var musicIndex: Int = -1

        var music: Music? = null
        musicList.forEachIndexed { _index, _music ->
          run {
            if (_music.id == id) {
              music = _music
              musicIndex = _index

              return@forEachIndexed
            }
          }
        }

        if (musicIndex > -1) {
          musicList.removeAt(musicIndex)

          Handler(handlerThread.looper).postDelayed({
            store.dispatch(GenreAction.GENRE_ACTION_UPDATE_PLAYLIST(Music.deepCloneMusicList(
              musicList)))

            mContext?.apply {
              CustomSnackbar.showSnackbar(CustomToastDataModel(
                "Delete music from playlist successful!",
                "Undo",
                object : ICustomSnackbarEventListener {
                  override fun onPrimaryButtonClickListener() {
                    musicList.add(musicIndex, music!!)

                    store.dispatch(GenreAction.GENRE_ACTION_UPDATE_PLAYLIST(Music.deepCloneMusicList(
                      musicList)))
                  }

                  override fun onSecondaryButtonClickListener() {
                  }
                }
              ),
                R.color.textColorNote)
            }
          }, 100)
        }
      }
    }

    fun updateRecentlyPlayedMusic(music: Music) {
      val musicList = getRecentlyPlayedMusic()

      var mMusic = music

      val index = musicList.indexOfFirst { it.id == mMusic.id }

      if (index == 0) return

      if (index > -1) {
        mMusic = musicList[index]

        musicList.removeAt(index)
      }

      mMusic.count_play++

      musicList.add(0, mMusic)

      Handler(handlerThread.looper).postDelayed({
        handleUpdateRecentlyPlayedFromSharedPreference(musicList)
      }, 10)
    }

    fun sortRecentlyPlayedListBy(type: EMusicListTypeSort) {
      store.state.genreState.apply {
        var musicList = recentlyPlayed

        Log.d("Binh", "musiclist: ${musicList[0].title}")

        when (type) {
          EMusicListTypeSort.SORT_BY_TITLE -> {
            Log.d("Binh", "Sort by title")

            musicList.sortBy { music -> music.title }
          }
          EMusicListTypeSort.SORT_BY_COUNT -> {
            Log.d("Binh", "Sort by count")

            musicList.sortByDescending { music -> music.count_play }
          }
        }

        Log.d("Binh", "musiclist afeter: ${musicList[0].title}")
        Handler(handlerThread.looper).postDelayed({
          handleUpdateRecentlyPlayedFromSharedPreference(musicList)
        }, 10)
      }
    }

    private fun handleUpdateRecentlyPlayedFromSharedPreference(musicList: MutableList<Music>) {
      getMusicSharedPreference()?.apply {
        edit().putString(RECENTLY_PLAYED_MUSIC_KEY, gson.toJson(musicList)).apply()
      }

      store.dispatch(GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(Music.deepCloneMusicList(
        musicList
      )))
    }
  }
}