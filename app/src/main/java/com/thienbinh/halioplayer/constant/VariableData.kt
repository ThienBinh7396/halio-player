package com.thienbinh.halioplayer.constant

import android.content.Context
import android.content.res.Resources
import android.os.Build
import kotlin.math.ceil

val SCALE_DP_PX = Resources.getSystem().displayMetrics.density

fun getStatusBarHeight(context: Context): Float {
  val resources = context.resources;
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
  return if (resourceId > 0)
    resources.getDimensionPixelSize(resourceId).toFloat()
  else
    ceil((if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 24 else 25) * resources.displayMetrics.density);
}