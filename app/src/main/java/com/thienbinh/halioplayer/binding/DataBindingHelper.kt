package com.thienbinh.halioplayer.binding

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.thienbinh.halioplayer.GlideApp
import com.thienbinh.halioplayer.utils.Helper


class DataBindingHelper {
  companion object {
    @BindingAdapter("app:bindCircleImage")
    @JvmStatic
    fun bindCircleImage(imageView: ImageView, src: Any?) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .circleCrop()
          .into(imageView)
      }
    }

    val mapSrcWithBitmapAfterGradient = mutableMapOf<String, Bitmap>()

    @BindingAdapter("app:bindCircleWithGradientImage")
    @JvmStatic
    fun bindCircleWithGradientImage(imageView: ImageView, src: Any?) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .circleCrop()
          .into(imageView)
      }
    }

    @BindingAdapter("app:bindGrayScale")
    @JvmStatic
    fun bindGrayScale(imageView: ImageView, someThing: Any) {
      val matrix = ColorMatrix()
      matrix.setSaturation(0.55f) //0 means grayscale

      val cf = ColorMatrixColorFilter(matrix)
      imageView.colorFilter = cf
    }

    @BindingAdapter("app:bindDarkenView")
    @JvmStatic
    fun bindDarkenView(imageView: ImageView, someThing: Any) {
      val matrix = floatArrayOf(
        0.22f, 0.22f, 0.22f, 0f, 0f,
        0.22f, 0.22f, 0.22f, 0f, 0f,
        0.22f, 0.22f, 0.22f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
      )

      val cf = ColorMatrixColorFilter(matrix)
      imageView.colorFilter = cf
    }

    data class AnimationWithIsAnimatingObject(
      val animation: Animator,
      var isAnimating: Boolean
    )

    private val mapViewTagWithIsAnimating = mutableMapOf<String, AnimationWithIsAnimatingObject>()

    private fun checkViewIsAnimating(
      view: View,
      viewTag: String,
      isAnimate: Boolean,
      typeAnimator: String = "rotation",
      animationDelay: Int = 0
    ): Boolean? {
      val checkViewTagExists = mapViewTagWithIsAnimating[viewTag]

      if (!isAnimate && checkViewTagExists == null) return null

      if (checkViewTagExists == null) {
        var animator: ObjectAnimator? = null

        when (typeAnimator) {
          "rotation" -> {
            animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f)
            animator?.duration = 20000
          }
          "scale" -> {
            animator = ObjectAnimator.ofPropertyValuesHolder(
              view,
              PropertyValuesHolder.ofFloat("scaleX", 0.875f),
              PropertyValuesHolder.ofFloat("scaleY", 0.875f),
              PropertyValuesHolder.ofFloat("alpha", 0.05f)
            )
            animator.startDelay = animationDelay.toLong()
            animator.duration = 3000
            animator.repeatMode = ObjectAnimator.REVERSE
          }
        }

        animator?.repeatCount = ObjectAnimator.INFINITE

        mapViewTagWithIsAnimating[viewTag] = AnimationWithIsAnimatingObject(
          animator!!,
          false
        )
        return null
      }
      return mapViewTagWithIsAnimating[viewTag]!!.isAnimating
    }

    @BindingAdapter("app:bindRotateViewAnimation")
    @JvmStatic
    fun bindRotateViewAnimation(view: View, isAnimate: Boolean) {
      var viewTag = view.tag ?: return

      viewTag = viewTag as String

      val checkViewIsAnimatingFromMap = checkViewIsAnimating(view, viewTag, isAnimate)

      if (!isAnimate && checkViewIsAnimatingFromMap == null) return

      if (isAnimate && checkViewIsAnimatingFromMap == null) {
        mapViewTagWithIsAnimating[viewTag]!!.animation.start()
        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = true

        return
      }

      if (isAnimate && !checkViewIsAnimatingFromMap!!) {
        mapViewTagWithIsAnimating[viewTag]!!.animation.resume()
        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = true

        return
      }

      if (!isAnimate && checkViewIsAnimatingFromMap != null && checkViewIsAnimatingFromMap) {
        mapViewTagWithIsAnimating[viewTag]!!.animation.pause()

        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = false
      }
    }

    @BindingAdapter(value = ["app:bindScaleViewAnimation", "app:bindAnimationDelay"])
    @JvmStatic
    fun bindScaleViewAnimation(view: View, isAnimate: Boolean, animationDelay: Int = 0) {
      var viewTag = view.tag ?: return

      viewTag = viewTag as String

      val checkViewIsAnimatingFromMap =
        checkViewIsAnimating(view, viewTag, isAnimate, "scale", animationDelay)

      if (!isAnimate && checkViewIsAnimatingFromMap == null) return

      if (isAnimate && checkViewIsAnimatingFromMap == null) {
        view.scaleX = 1f
        view.scaleY = 1f

        mapViewTagWithIsAnimating[viewTag]!!.animation.start()
        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = true

        return
      }

      if (isAnimate && !checkViewIsAnimatingFromMap!!) {
        mapViewTagWithIsAnimating[viewTag]!!.animation.resume()
        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = true

        return
      }

      if (!isAnimate && checkViewIsAnimatingFromMap != null && checkViewIsAnimatingFromMap) {
        mapViewTagWithIsAnimating[viewTag]!!.animation.pause()

        mapViewTagWithIsAnimating[viewTag]!!.isAnimating = false
      }
    }

    @BindingAdapter("app:bindMusicDurationText")
    @JvmStatic
    fun bindMusicDurationText(textView: TextView, time: Int?) {
      if (time == null) return

      textView.text = Helper.formatMusicDuration(time.toLong())
    }
  }
}