package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EFragmentName
import com.thienbinh.halioplayer.constant.SCALE_DP_PX
import com.thienbinh.halioplayer.databinding.FragmentAlbumDetailsBinding
import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.utils.Helper
import kotlin.math.abs

class AlbumDetailsFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
  private lateinit var mFragmentAlbumDetailsBinding: FragmentAlbumDetailsBinding

  private var mAlbum: Album? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    getDataFromBundle()

    mFragmentAlbumDetailsBinding = DataBindingUtil.inflate<FragmentAlbumDetailsBinding>(
      inflater,
      R.layout.fragment_album_details,
      null,
      false
    ).apply {
      if (mAlbum != null)
        album = mAlbum

      goBack.setOnClickListener {
        MainActivity.navigate(R.id.action_albumDetailsFragment_to_homeFragment)
      }

      mainAppbarLayout.addOnOffsetChangedListener(this@AlbumDetailsFragment)
    }

    return mFragmentAlbumDetailsBinding.root
  }

  private fun getDataFromBundle() {
    val intent = arguments?.getSerializable("album")

    if (intent != null && intent is Album)
      mAlbum = intent
  }

  private var lastOffset: Int = 0

  override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

    if (abs(lastOffset - verticalOffset) < 2) return

    lastOffset = verticalOffset

    appBarLayout?.apply {
      val verticalOffsetRate = abs(verticalOffset) * 1f / totalScrollRange

      mFragmentAlbumDetailsBinding.apply {
        tvTitleAlbum.setTextSize(TypedValue.COMPLEX_UNIT_SP, (24 - 7 * verticalOffsetRate))

        toolbarContent.updatePadding(left = (38 * SCALE_DP_PX * verticalOffsetRate).toInt())

        tvTitleAlbum.updatePadding(bottom = ((6 - 6 * verticalOffsetRate) * SCALE_DP_PX).toInt())

        tvCaptionAlbum.updatePadding(bottom = ((20 - 16 * verticalOffsetRate) * SCALE_DP_PX).toInt())

        tvBack.alpha = 1 - verticalOffsetRate
      }
    }
    Log.d("Binh", "Offset $verticalOffset ${context?.let { Helper.spToPx(6f, it) }} ")
  }

  override fun onStart() {
    super.onStart()

    Log.d("Binh", "Recently Fragment")
    MainActivity.mFragmentName = EFragmentName.ALBUM_FRAGMENT
  }
}