package com.example.shoptify.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.thienbinh.halioplayer.R

class ScaleClickView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
  private val scaleWithReverseAnim = AnimationUtils.loadAnimation(context, R.anim.scale_view_with_reverse_anim)

  init {
    setOnClickListener {
      startAnimation(scaleWithReverseAnim)
    }
  }
}