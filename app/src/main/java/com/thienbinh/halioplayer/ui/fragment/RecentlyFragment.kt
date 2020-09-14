package com.thienbinh.halioplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EFragmentName
import com.thienbinh.halioplayer.constant.EMusicListTypeSort
import com.thienbinh.halioplayer.databinding.FragmentRecentlyBinding
import com.thienbinh.halioplayer.sharePreference.MusicSharePreference
import com.thienbinh.halioplayer.viewModel.GenreViewModel
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class RecentlyFragment : Fragment() {
  private var mPopupMenu: PopupMenu? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {

    val mFragmentRecentlyBinding = DataBindingUtil.inflate<FragmentRecentlyBinding>(
      inflater,
      R.layout.fragment_recently,
      container,
      false
    ).apply {
      genreViewModel = GenreViewModel()

      musicViewModel = MusicStoreViewModel()

      ibnBack.setOnClickListener {
        MainActivity.navigate(R.id.action_recentlyFragment_to_homeFragment)
      }

      mPopupMenu = PopupMenu(
        requireContext(),
        btnSort
      ).apply {
        menuInflater.inflate(R.menu.genre_options_menu, menu)

        setOnMenuItemClickListener {
          return@setOnMenuItemClickListener when (it.itemId) {
            R.id.sort_by_title -> {
              MusicSharePreference.sortRecentlyPlayedListBy(EMusicListTypeSort.SORT_BY_TITLE)
              true
            }
            R.id.sort_by_count -> {
              MusicSharePreference.sortRecentlyPlayedListBy(EMusicListTypeSort.SORT_BY_COUNT)
              true
            }
            else -> true
          }
        }
      }

      btnSort.setOnClickListener {
        mPopupMenu?.show()
      }
    }

    return mFragmentRecentlyBinding.root
  }

  override fun onStart() {
    super.onStart()

    Log.d("Binh", "Recently Fragment")
    MainActivity.mFragmentName = EFragmentName.RECENT_FRAGMENT
  }
}