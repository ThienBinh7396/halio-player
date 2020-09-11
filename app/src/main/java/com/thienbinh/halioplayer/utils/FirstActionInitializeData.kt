package com.thienbinh.halioplayer.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.thienbinh.halioplayer.constant.VariableData
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

      Genre.createInstance()
      Album.createInstance()
      Music.initializeList()

      if (Genre.getGenreById(0)!!.musicList.size == 0) {
        Genre.mapMusicToGenre()

        Album.mapMusicToAlbum()

        store.dispatch(GenreAction.GENRE_ACTION_UPDATE_RECENTLY_PLAYED_LIST(MusicSharePreference.getRecentlyPlayedMusic()))
      }
    }

    fun loadMusicFromDevice(context: Context, isForced: Boolean = false) {
      Log.d("Binh", "Permission: ${store.state.permissionState.readExternalStoragePermissionState}")

      store.state.permissionState.apply {
        if (!readExternalStoragePermissionState || (!isForced && isFirstLoadMusicFromDevice)) {
          return
        }
      }

      val musicResolver = context.contentResolver
      val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

      val projection = arrayOf(
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Albums.ALBUM_ID
      )

      val musicCursor = musicResolver.query(musicUri, projection, null, null, null, null)

      if (musicCursor != null) {

        val columnIndexPath = musicCursor.getColumnIndexOrThrow(projection[0])
        val columnIndexDisplayName = musicCursor.getColumnIndexOrThrow(projection[1])
        val columnIndexArtist = musicCursor.getColumnIndexOrThrow(projection[2])
        val columnIndexDuration = musicCursor.getColumnIndexOrThrow(projection[3])
        val columnIndexThumbnail = musicCursor.getColumnIndexOrThrow(projection[4])

        val metaRetriever = MediaMetadataRetriever()

        while (musicCursor.moveToNext()) {
          val contentUri = musicCursor.getString(columnIndexPath)

          metaRetriever.setDataSource(contentUri)

          val thumbnailArt = metaRetriever.embeddedPicture

          val thumbnailBitmap = if (thumbnailArt != null) {
            BitmapFactory.decodeByteArray(
              thumbnailArt,
              0,
              thumbnailArt.size
            )
          } else
            null

          val artist = musicCursor.getString(columnIndexArtist)
          val duration = musicCursor.getLong(columnIndexDuration)
          val name = musicCursor.getString(columnIndexDisplayName)

          Log.d("Binh", "XXX: $contentUri $artist $duration ${name.hashCode()} $thumbnailBitmap")
        }

      }

      musicCursor?.close()

      store.dispatch(PermissionAction.PERMISSION_ACTION_UPDATE_IS_FIRST_LOAD_MUSIC(true))
    }
  }
}