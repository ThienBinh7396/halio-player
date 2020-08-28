package com.thienbinh.halioplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest


class RotateImageTransformation(context: Context?, rotate: Int) :
  BitmapTransformation() {
  private var rotate = 0

  init {
    this.rotate = rotate % 360
  }


  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update("rotate${rotate}".toByteArray());
  }


  override fun transform(
    pool: BitmapPool,
    toTransform: Bitmap,
    outWidth: Int,
    outHeight: Int
  ): Bitmap {
    val matrix = Matrix()

    matrix.postRotate(rotate * 1f)
    return Bitmap.createBitmap(toTransform, 0, 0, outWidth, outHeight, matrix, false);
  }
}
