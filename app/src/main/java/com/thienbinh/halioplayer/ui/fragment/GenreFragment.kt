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
import com.thienbinh.halioplayer.databinding.FragmentAlbumDetailsBinding
import com.thienbinh.halioplayer.databinding.FragmentGenreBinding
import com.thienbinh.halioplayer.model.Album
import com.thienbinh.halioplayer.model.Genre
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel


class GenreFragment : Fragment() {
  private lateinit var mFragmentGenreBinding: FragmentGenreBinding

  private var mGenre: Genre? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    getDataFromBundle()

    mFragmentGenreBinding = DataBindingUtil.inflate<FragmentGenreBinding>(
      inflater,
      R.layout.fragment_genre,
      container,
      false
    ).apply {
      if (mGenre != null)
        genre = mGenre

      ibnBack.setOnClickListener {
        MainActivity.navigate(R.id.action_genreFragment_to_homeFragment)
      }

      genre = mGenre

      musicViewModel = MusicStoreViewModel()

    }

    return mFragmentGenreBinding.root
  }


  private fun getDataFromBundle() {
    val intent = arguments?.getSerializable("genre")

    if (intent != null && intent is Genre)
      mGenre = intent
  }

  override fun onStart() {
    super.onStart()

    Log.d("Binh", "Genre Fragment")
    MainActivity.mFragmentName = EFragmentName.GENRE_FRAGMENT
  }
}