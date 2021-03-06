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
import com.thienbinh.halioplayer.databinding.FragmentHomeBinding
import com.thienbinh.halioplayer.viewModel.GenreViewModel

class HomeFragment : Fragment() {
  private lateinit var mFragmentHomeBinding: FragmentHomeBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mFragmentHomeBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
      inflater,
      R.layout.fragment_home,
      container,
      false
    ).apply {
      mGenreViewModel = GenreViewModel()

      recentPlayedLayout.showMore.setOnClickListener {
        MainActivity.navigate(R.id.action_homeFragment_to_recentlyFragment)
      }
    }

    return mFragmentHomeBinding.root
  }

  override fun onStart() {
    super.onStart()
    Log.d("Binh", "Home Fragment")
    MainActivity.mFragmentName = EFragmentName.HOME_FRAGMENT
  }
}