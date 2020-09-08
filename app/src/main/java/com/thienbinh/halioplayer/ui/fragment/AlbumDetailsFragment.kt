package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.FragmentAlbumDetailsBinding
import com.thienbinh.halioplayer.model.Album

class AlbumDetailsFragment : Fragment() {
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
    }

    return mFragmentAlbumDetailsBinding.root
  }

  private fun getDataFromBundle() {
    val intent = arguments?.getSerializable("album")

    if (intent != null && intent is Album)
      mAlbum = intent
  }
}