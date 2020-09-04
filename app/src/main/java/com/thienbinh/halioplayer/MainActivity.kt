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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.customInterface.IMainActivityEventListener
import com.thienbinh.halioplayer.customInterface.IMusicControlEventListener
import com.thienbinh.halioplayer.databinding.ActivityMainBinding
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.service.MusicService
import com.thienbinh.halioplayer.utils.FirstActionInitializeData
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class MainActivity : AppCompatActivity(), IMusicControlEventListener, IMainActivityEventListener {
  companion object {
    private var navControllerMainActivity: NavController? = null

    fun navigate(id: Int, bundle: Bundle? = null) {
      navControllerMainActivity?.navigate(id, bundle)
    }
  }

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

      mMusicStoreViewModel = MusicStoreViewModel(this@MainActivity, this@MainActivity)

      bottomMusicSheet.expandedSheetLayout.layoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        Resources.getSystem().displayMetrics.heightPixels - getStatusBarHeight(this@MainActivity).toInt()
      )

      navControllerMainActivity = findNavController(R.id.nav_host)
    }

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    startMusicService()

    FirstActionInitializeData.initialize(this)
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

      }
    })
  }

  private fun startMusicService() {
    val intent = Intent(this@MainActivity, MusicService::class.java)
    intent.action = ACTION_MUSIC_INITIALIZE
    startService(intent)
  }

  override fun onBackPressed() {
    if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
      mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
  }

  override fun onToggleButtonClickListener() {
    if (store.state.musicState.currentMusic == null) {
      Toast.makeText(this, "Choose at least one music to play!", Toast.LENGTH_SHORT).show()
      return
    }

    val intent = Intent()
    intent.action = ACTION_MUSIC_TOGGLE
    sendBroadcast(intent)
  }

  override fun toggleStateMusicBottomSheet(isExpanded: Boolean) {
    try {
      mBottomSheetBehavior.state =
        if (isExpanded) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
    } catch (err: Error) {
      Toast.makeText(applicationContext, err.message, Toast.LENGTH_SHORT).show()
    }
  }
}
