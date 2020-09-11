package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EFragmentName
import com.thienbinh.halioplayer.databinding.FragmentPlaylistBinding
import com.thienbinh.halioplayer.viewModel.GenreViewModel
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class PlaylistFragment : Fragment() {
  private lateinit var mFragmentPlaylistBinding: FragmentPlaylistBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mFragmentPlaylistBinding = DataBindingUtil.inflate<FragmentPlaylistBinding>(
      inflater,
      R.layout.fragment_playlist,
      null,
      false
    ).apply {
      mMusicStoreViewModel = MusicStoreViewModel()

      mGenreViewModel = GenreViewModel()
    }

    return mFragmentPlaylistBinding.root
  }

  override fun onStart() {
    super.onStart()

    Log.d("Binh", "Playlist fragment")
    MainActivity.mFragmentName = EFragmentName.PLAYLIST_FRAGMENT
  }

}