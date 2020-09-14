package com.thienbinh.halioplayer.utils

import android.graphics.Bitmap
import android.util.Log

class MapContentUriWithBitmap {
  companion object {
    private val mapContentUriWithBitmap = mutableMapOf<String, Bitmap?>()

    @JvmStatic
    fun getBitmapByContentUri(uri: String?): Bitmap{
      return mapContentUriWithBitmap[uri]!!
    }

    fun addUriToMap(uri: String, bitmap: Bitmap?) {
      if (mapContentUriWithBitmap.containsKey(uri)) return

      mapContentUriWithBitmap[uri] = bitmap

      Log.d("Binh", "URI: $uri")
    }
  }
}