package com.thienbinh.halioplayer.ui.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.thienbinh.halioplayer.MainActivity
import com.thienbinh.halioplayer.R
import com.thienbinh.halioplayer.constant.EFragmentName
import com.thienbinh.halioplayer.databinding.FragmentPlaylistBinding
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.PermissionAction
import com.thienbinh.halioplayer.utils.FirstActionInitializeData
import com.thienbinh.halioplayer.utils.RequestPermissionRuntime
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

      reloadMusicFromDevice.setOnClickListener {

        val checkPermission =
          RequestPermissionRuntime.checkPermissionReadExternalStorage(requireContext())


        if (checkPermission) FirstActionInitializeData.loadMusicFromDevice(requireContext(), true)
      }
    }

    return mFragmentPlaylistBinding.root
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      RequestPermissionRuntime.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        store.dispatch(
          PermissionAction.PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION(
            true
          )
        )

        FirstActionInitializeData.loadMusicFromDevice(requireContext(), true)
      } else {
        Toast.makeText(
          requireContext(), "Read external storage permission denied",
          Toast.LENGTH_SHORT
        ).show()

        store.dispatch(
          PermissionAction.PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION(
            false
          )
        )

      }
      else -> super.onRequestPermissionsResult(
        requestCode, permissions!!,
        grantResults
      )
    }
  }

  override fun onStart() {
    super.onStart()

    Log.d("Binh", "Playlist fragment")
    MainActivity.mFragmentName = EFragmentName.PLAYLIST_FRAGMENT
  }

}