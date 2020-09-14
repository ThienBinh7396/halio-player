package com.thienbinh.halioplayer.store.middleware

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.thienbinh.halioplayer.MainApplication
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_DATA_BUNDLE
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_DATA_BUNDLE_MUSIC
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_UPDATE
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.MusicAction
import com.thienbinh.halioplayer.store.state.RootState
import org.rekotlin.Middleware

internal val musicMiddleware: Middleware<RootState> = { dispatch, _ ->
  { next ->
    { action ->
      run {
        when (action) {
          is MusicAction.MUSIC_ACTION_CONTROL_LIST_MUSIC -> {
            val intent = Intent()

            val bundle = Bundle()

            store.state.apply {
              if (musicState.currentMusic != null) {
                val getIndexInPlaylist =
                  genreState.playlists.indexOfFirst { it.id == musicState.currentMusic!!.id }

                if (getIndexInPlaylist < 0) return@apply

                var music: Music? = null

                music = if (action.isNext) {
                  if (getIndexInPlaylist < genreState.playlists.size - 1) genreState.playlists[getIndexInPlaylist + 1] else
                    genreState.playlists[0]
                }else{
                  if (getIndexInPlaylist > 0) genreState.playlists[getIndexInPlaylist - 1] else
                    genreState.playlists[genreState.playlists.size - 1]
                }

                if (music != null) {
                  bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, music)

                  intent.action = ACTION_MUSIC_UPDATE
                  intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

                  MainApplication.currentActivity?.sendBroadcast(intent)
                } else {
                  MainApplication.currentActivity?.apply {
                    Toast.makeText(
                      MainApplication.currentActivity,
                      "You are at ${if (action.isNext) "end" else "start"} of list!",
                      Toast.LENGTH_SHORT
                    ).show()
                  }
                }
              }
            }
          }
        }

        next(action)
      }
    }
  }
}