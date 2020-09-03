package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.databinding.FragmentRecentlyBinding
import com.thienbinh.halioplayer.viewModel.GenreViewModel

class RecentlyFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val mFragmentRecentlyBinding = DataBindingUtil.inflate<FragmentRecentlyBinding>(
      inflater,
      R.layout.fragment_recently,
      container,
      false
    ).apply {
      genreViewModel = GenreViewModel()

      ibnBack.setOnClickListener {
        MainActivity.navigate(R.id.action_recentlyFragment_to_homeFragment)
      }
    }

    return mFragmentRecentlyBinding.root
  }
}