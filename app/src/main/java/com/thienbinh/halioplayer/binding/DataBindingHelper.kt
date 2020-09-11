package com.thienbinh.halioplayer.binding

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.thienbinh.halioplayer.GlideApp
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.adapter.*
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.customInterface.IMusicBlockEventListener
import com.thienbinh.halioplayer.model.Lyric
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.utils.CenterSmpothScroller
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

    private var enterSlideBottomAnimation: Animation? = null
    private var exitSlideBottomAnimation: Animation? = null

    @BindingAdapter(value = ["app:hideView", "app:withSlideBottomAnimation"], requireAll = false)
    @JvmStatic
    fun hideView(view: View, isShow: Boolean, withSlideBottomAnimation: Boolean = false) {
      if (withSlideBottomAnimation) {
        if (enterSlideBottomAnimation == null) {
          enterSlideBottomAnimation = AnimationUtils.loadAnimation(
            view.context.applicationContext,
            R.anim.enter_slide_bottom_anim
          )
        }

        if (exitSlideBottomAnimation == null) {
          exitSlideBottomAnimation = AnimationUtils.loadAnimation(
            view.context.applicationContext,
            R.anim.exit_slide_bottom_anim
          )
        }

        if (isShow) {
          enterSlideBottomAnimation?.start()
        } else {
          exitSlideBottomAnimation?.start()
        }
      }

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
    fun bindDarkenView(imageView: ImageView, someThing: Boolean = true) {
      if (!someThing) return

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
      animationDelay: Int = 0,
      isFastAnimation: Boolean = false
    ): Boolean? {
      val checkViewTagExists = mapViewTagWithIsAnimating[viewTag]

      if (!isAnimate && checkViewTagExists == null) return null

      if (checkViewTagExists == null) {
        var animator: ObjectAnimator? = null

        when (typeAnimator) {
          "rotation" -> {
            animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f)
            animator?.duration = if(isFastAnimation) 2500 else 20000
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

    @BindingAdapter(value=["app:bindRotateViewAnimation", "app:bindIsFastAnimation"], requireAll = false)
    @JvmStatic
    fun bindRotateViewAnimation(view: View, isAnimate: Boolean, isFastAnimation: Boolean) {
      var viewTag = view.tag ?: return

      viewTag = viewTag as String

      val checkViewIsAnimatingFromMap = checkViewIsAnimating(view, viewTag, isAnimate, isFastAnimation = isFastAnimation)

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

    @BindingAdapter(
      value = ["app:bindMusicList", "app:bindMusicListDisplayStyle", "app:bindIsShowWidgetButton", "app:bindWidgetButtonType"],
      requireAll = false
    )
    @JvmStatic
    fun bindMusicList(
      rcv: RecyclerView,
      musicList: MutableList<Music>? = null,
      displayStyle: EDisplayStyle = EDisplayStyle.BLOCK_STYLE,
      isShowWidgetButton: Boolean = false,
      widgetButtonType: ETypeWidgetButton? = null
    ) {
      var adapter: MusicListAdapter

      if (rcv.adapter == null) {

        val musicBlockEventListener = object : IMusicBlockEventListener {
          override fun onWidgetButtonClickListener(musicId: Int) {
            when (widgetButtonType) {
              ETypeWidgetButton.REMOVE_FROM_RECENTLY_PLAYED_LIST -> {
                Log.d("Binh", "Position: $musicId")
                MusicSharePreference.removeMusicWhere(musicId)
              }
            }
          }

          override fun onContainerClickListener(music: Music) {
            val intent = Intent()

            val bundle = Bundle()
            bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, music)

            intent.action = ACTION_MUSIC_UPDATE
            intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

            rcv.context.sendBroadcast(intent)
          }
        }

        adapter = MusicListAdapter(
          displayStyle = displayStyle,
          isShowWidgetButton = isShowWidgetButton,
          eventListener = musicBlockEventListener
        )

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
        else {
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
                  bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, music)

                  intent.action = ACTION_MUSIC_UPDATE
                  intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

                  rcv.context.sendBroadcast(intent)
                }

                override fun onLongClick(view: View?, position: Int) {
                }
              })
          )
        }
      }

      adapter = rcv.adapter as MusicListAdapter

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

    @BindingAdapter(value = ["app:bindAlbumList"])
    @JvmStatic
    fun bindAlbumList(
      rcv: RecyclerView,
      someThing: Any
    ) {
      if (rcv.adapter == null) {
        val adapter = AlbumListAdapter()
        rcv.adapter = adapter

        rcv.layoutManager = LinearLayoutManager(
          rcv.context,
          LinearLayoutManager.HORIZONTAL,
          false
        )

        rcv.addItemDecoration(SpaceItemDecoration(0, 12 * SCALE_DP_PX.toInt()))

        rcv.addOnItemTouchListener(RecyclerViewTouchListener(
          rcv.context,
          rcv,
          object : RecyclerViewTouchListener.ClickListener {
            override fun onClick(view: View?, position: Int) {
              val bundle = bundleOf("album" to adapter.getItemAt(position))

              MainActivity.navigate(R.id.action_homeFragment_to_albumDetailsFragment, bundle)
            }

            override fun onLongClick(view: View?, position: Int) {
            }
          }
        ))
      }
    }

    @BindingAdapter(value = ["app:bindGift"], requireAll = false)
    @JvmStatic
    fun bindGift(imageView: ImageView, gift: Any?) {
      if (gift != null) {
        Glide.with(imageView.context)
          .asGif()
          .fitCenter()
          .load(gift)
          .into(imageView)

      }
    }

    @BindingAdapter(value = ["app:bindGradientLyricText", "app:bindPosition"])
    @JvmStatic
    fun bindGradientText(textView: TextView, isBinding: Boolean, position: Int = 0) {
      Log.d("Binh", "IS Binding: $isBinding ${textView.height} , $position")

      if (isBinding) {
        var shaderGradientLyric = LinearGradient(
          0f, 0f, 0f, textView.height.toFloat(),
          Color.parseColor("#2d2d2d"),
          Color.parseColor("#d8d8d8"),
          Shader.TileMode.CLAMP
        )

        textView.paint.shader = shaderGradientLyric
      } else {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
          textView.setTextColor(textView.resources.getColor(R.color.colorWhite, null))
        } else {
          textView.setTextColor(textView.resources.getColor(R.color.colorWhite))
        }
      }
    }

    @BindingAdapter("app:bindLyricText")
    @JvmStatic
    fun bindLyricText(rcv: RecyclerView, lyrics: MutableList<Lyric>?) {
      var adapter = rcv.adapter ?: LyricTextAdapter()

      if (rcv.adapter == null) {
        rcv.adapter = adapter

        rcv.layoutManager = object : GridLayoutManager(
          rcv.context,
          1,
          GridLayoutManager.VERTICAL,
          false
        ) {
          override fun smoothScrollToPosition(
            recyclerView: RecyclerView?,
            state: RecyclerView.State?,
            position: Int
          ) {
            val smoothScroller = CenterSmpothScroller(recyclerView!!.context)
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
          }
        }


        val snapHelper = PagerSnapHelper()

        snapHelper.attachToRecyclerView(rcv)
        rcv.onFlingListener = snapHelper
      }

      adapter = adapter as LyricTextAdapter

      if (lyrics != null) {
        adapter.updateList(lyrics)
      }
    }
  }
}
