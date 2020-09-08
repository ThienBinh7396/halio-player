package com.thienbinh.halioplayer.utils

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

class CenterSmpothScroller(context: Context): LinearSmoothScroller(context) {
  override fun calculateDtToFit(
    viewStart: Int,
    viewEnd: Int,
    boxStart: Int,
    boxEnd: Int,
    snapPreference: Int
  ): Int {
    return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
  }

  override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
    return 12.5f / (displayMetrics?.densityDpi ?: 1)
  }
}