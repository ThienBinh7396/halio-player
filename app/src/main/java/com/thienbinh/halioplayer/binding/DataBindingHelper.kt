package com.thienbinh.halioplayer.binding

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thienbinh.halioplayer.GlideApp
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.adapter.EDisplayStyle
import com.thienbinh.halioplayer.adapter.GenreListAdapter
import com.thienbinh.halioplayer.adapter.MusicListAdapter
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_DATA_BUNDLE
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_DATA_BUNDLE_MUSIC
import com.thienbinh.halioplayer.constant.ACTION_MUSIC_UPDATE
import com.thienbinh.halioplayer.constant.SCALE_DP_PX
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.utils.Helper
import com.thienbinh.halioplayer.utils.RecyclerViewTouchListener
import com.thienbinh.halioplayer.utils.SpaceItemDecoration


class DataBindingHelper {
  companion object {
    @BindingAdapter("app:showUnless")
    @JvmStatic
    fun showUnless(view: View, isShow: Boolean) {
      view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:hideView")
    @JvmStatic
    fun hideView(view: View, isShow: Boolean) {
      view.visibility = if (!isShow) View.INVISIBLE else View.VISIBLE
    }


    @BindingAdapter("app:bindSrcImage")
    @JvmStatic
    fun bindSrcImage(imageView: ImageView, src: Any?) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .into(imageView)
      }
    }

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
        0.2f, 0.2f, 0.2f, 0f, 0f,
        0.2f, 0.2f, 0.2f, 0f, 0f,
        0.2f, 0.2f, 0.2f, 0f, 0f,
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

    @BindingAdapter(value = ["app:bindMusicList", "app:bindMusicListDisplayStyle"])
    @JvmStatic
    fun bindMusicList(
      rcv: RecyclerView,
      musicList: MutableList<Music>? = null,
      displayStyle: EDisplayStyle = EDisplayStyle.BLOCK_STYLE
    ) {
      val adapter =
        if (rcv.adapter == null) MusicListAdapter(displayStyle = displayStyle) else (rcv.adapter as MusicListAdapter)

      if (rcv.adapter == null) {
        rcv.setHasFixedSize(true)

        rcv.adapter = adapter

        rcv.layoutManager = if (displayStyle == EDisplayStyle.BLOCK_STYLE) LinearLayoutManager(
          rcv.context,
          LinearLayoutManager.HORIZONTAL,
          false
        ) else
          GridLayoutManager(
            rcv.context,
            1,
            GridLayoutManager.VERTICAL,
            false
          )

        if (displayStyle == EDisplayStyle.BLOCK_STYLE)
          rcv.addItemDecoration(SpaceItemDecoration(0, 12 * SCALE_DP_PX.toInt()))
        else
          rcv.addItemDecoration(SpaceItemDecoration(18 * SCALE_DP_PX.toInt(), 0))

        rcv.addOnItemTouchListener(
          RecyclerViewTouchListener(
            rcv.context,
            rcv,
            object : RecyclerViewTouchListener.ClickListener {
              override fun onClick(view: View?, position: Int) {
                val music = adapter.getItemAt(position) ?: return

                val intent = Intent()

                val bundle = Bundle()
                bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, adapter.getItemAt(position))

                intent.action = ACTION_MUSIC_UPDATE
                intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

                rcv.context.sendBroadcast(intent)
              }

              override fun onLongClick(view: View?, position: Int) {
              }
            })
        )
      }

      if (musicList != null) {
        adapter.updateList(musicList)
      }
    }

    @BindingAdapter(value = ["app:bindGenreList"])
    @JvmStatic
    fun bindGenreList(
      rcv: RecyclerView,
      someThing: Any
    ) {
      if (rcv.adapter == null) {
        rcv.adapter = GenreListAdapter()

        rcv.layoutManager = LinearLayoutManager(
          rcv.context,
          LinearLayoutManager.HORIZONTAL,
          false
        )

        rcv.addItemDecoration(SpaceItemDecoration(0, 12 * SCALE_DP_PX.toInt()))
      }
    }

    @BindingAdapter("app:bindGift")
    @JvmStatic
    fun bindGift(imageView: ImageView, gift: Any?) {
      if (gift != null) {
        Log.d("Binh", "Bind gift: $gift")

        Glide.with(imageView.context)
          .asGif()
          .load(gift)
          .into(imageView)
      }
    }
  }
}
