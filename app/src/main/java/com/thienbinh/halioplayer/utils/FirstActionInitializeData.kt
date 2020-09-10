package com.thienbinh.halioplayer.utils

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.GenreAction
import com.thienbinh.halioplayer.store.action.PermissionAction

class FirstActionInitializeData {
  companion object {
    fun initialize(context: Context) {
      store.dispatch(
        PermissionAction.PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION(
          RequestPermissionRuntime.checkPermissionReadExternalStorage(context)
        )
      )

      Genre.createInstance()
      Album.createInstance()
      Music.initializeList()

      loadMusicFromDevice(context)

      if (Genre.getGenreById(0)!!.musicList.size == 0) {
        Genre.mapMusicToGenre()

        Album.mapMusicToAlbum()

        store.dispatch(GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(MusicSharePreference.getRecentlyPlayedMusic()))
      }
    }

    fun loadMusicFromDevice(context: Context) {
      Log.d("Binh", "Permission: ${store.state.permissionState.readExternalStoragePermissionState}")

      if (!store.state.permissionState.readExternalStoragePermissionState) {
        return
      }

      val musicResolver = context.contentResolver
      val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

      val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TRACK
      )

      val musicCursor = musicResolver.query(musicUri, projection, null, null, null, null)

      if (musicCursor != null) {

        val columnIndexPath = musicCursor.getColumnIndexOrThrow(projection[0])
        val columnIndexDisplayName = musicCursor.getColumnIndexOrThrow(projection[1])
        val columnIndexArtist = musicCursor.getColumnIndexOrThrow(projection[2])
        val columnIndexDuration = musicCursor.getColumnIndexOrThrow(projection[3])
        val columnIndexDocumentID = musicCursor.getColumnIndexOrThrow(projection[4])

        while (musicCursor.moveToNext()) {
          val contentUri = ContentUris.withAppendedId(musicUri, musicCursor.getLong(columnIndexPath))

          val artist = musicCursor.getString(columnIndexArtist)
          val duration = musicCursor.getLong(columnIndexDuration)
          val name = musicCursor.getString(columnIndexDisplayName)
          val id = musicCursor.getString(columnIndexDocumentID)

          Log.d("Binh", "XXX: $contentUri $artist $duration ${name.hashCode()}")
        }

      }

      musicCursor?.close()
    }
  }
}