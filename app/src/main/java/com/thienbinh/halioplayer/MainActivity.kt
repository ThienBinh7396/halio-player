package com.thienbinh.halioplayer

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.customInterface.IMusicControlEventListener
import com.thienbinh.halioplayer.databinding.ActivityMainBinding
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.service.MusicService
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class MainActivity : AppCompatActivity(), IMusicControlEventListener {
  private lateinit var mActivityMainBinding: ActivityMainBinding

  private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    mActivityMainBinding = DataBindingUtil.inflate(
      LayoutInflater.from(this),
      R.layout.activity_main,
      null,
      false
    )

    mActivityMainBinding.apply {
      setContentView(root)

      initView()

      mMusicStoreViewModel = MusicStoreViewModel(this@MainActivity)

      bottomMusicSheet.expandedSheetLayout.layoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        Resources.getSystem().displayMetrics.heightPixels - getStatusBarHeight(this@MainActivity).toInt()
      )
    }

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    startMusicService()
  }

  private fun initView() {
    mBottomSheetBehavior =
      BottomSheetBehavior.from(mActivityMainBinding.bottomMusicSheet.bottomMusicSheetContainer)

    val expandedTotalTranslateY = resources.getDimension(
      R.dimen.bottom_music_sheet_collapsed_content_height
    )

    mBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {
        mActivityMainBinding.bottomMusicSheet.expandedSheetLayout.visibility =
          if (slideOffset > 0.1) View.VISIBLE else View.INVISIBLE

        mActivityMainBinding.bottomMusicSheet.expandedSheetLayout.translationY =
          (1 - slideOffset) * expandedTotalTranslateY
        Log.d("Binh", "Bottom sheet call ${bottomSheet.id} slide $slideOffset")

      }
    })
  }

  private fun startMusic() {
    val intent = Intent()

    val bundle = Bundle()
    bundle.putSerializable(ACTION_MUSIC_DATA_BUNDLE_MUSIC, Music.getMusicById(1))

    intent.action = ACTION_MUSIC_UPDATE
    intent.putExtra(ACTION_MUSIC_DATA_BUNDLE, bundle)

    sendBroadcast(intent)
  }

  private fun startMusicService() {
    val intent = Intent(this@MainActivity, MusicService::class.java)
    intent.action = ACTION_MUSIC_INITIALIZE
    startService(intent)
  }

  override fun onToggleButtonClickListener() {
    if (store.state.musicState.currentMusic == null) {
      startMusic()
      return
    }

    val intent = Intent()
    intent.action = ACTION_MUSIC_TOGGLE
    sendBroadcast(intent)
  }
}
