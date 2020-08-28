package com.thienbinh.halioplayer.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.thienbinh.halioplayer.GlideApp

class DataBindingHelper {
  companion object {
    @BindingAdapter("app:bindCircleImage")
    @JvmStatic
    fun bindCircleImage(imageView: ImageView, src: Any?) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .into(imageView)
      }
    }
  }
}