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
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thienbinh.halioplayer.constant.*
import com.thienbinh.halioplayer.customInterface.IMainActivityEventListener
import com.thienbinh.halioplayer.customInterface.IMainTabLayoutClickListener
import com.thienbinh.halioplayer.customInterface.IMusicControlEventListener
import com.thienbinh.halioplayer.databinding.ActivityMainBinding
import com.thienbinh.halioplayer.model.Music
import com.thienbinh.halioplayer.service.MusicService
import com.thienbinh.halioplayer.ui.snackbar.CustomSnackbar
import com.thienbinh.halioplayer.utils.FirstActionInitializeData
import com.thienbinh.halioplayer.viewModel.MusicStoreViewModel

class MainActivity : AppCompatActivity(), IMusicControlEventListener, IMainActivityEventListener,
  IMainTabLayoutClickListener {
  companion object {
    private var navControllerMainActivity: NavController? = null

    var mFragmentName = EFragmentName.HOME_FRAGMENT

    private val mapFragmentWithDestinationId: Map<EFragmentName, Map<EFragmentName, Int>> = mapOf(
      EFragmentName.HOME_FRAGMENT to mapOf(
        EFragmentName.RECENT_FRAGMENT to R.id.action_homeFragment_to_recentlyFragment,
        EFragmentName.LYRIC_FRAGMENT to R.id.action_homeFragment_to_lyricsFragment,
        EFragmentName.ALBUM_FRAGMENT to R.id.action_homeFragment_to_albumDetailsFragment,
        EFragmentName.PLAYLIST_FRAGMENT to R.id.action_homeFragment_to_playlistFragment
      ),
      EFragmentName.PLAYLIST_FRAGMENT to mapOf(
        EFragmentName.HOME_FRAGMENT to R.id.action_playlistFragment_to_homeFragment
      ),
      EFragmentName.RECENT_FRAGMENT to mapOf(
        EFragmentName.HOME_FRAGMENT to R.id.action_recentlyFragment_to_homeFragment,
        EFragmentName.LYRIC_FRAGMENT to R.id.action_recentlyFragment_to_lyricsFragment,
        EFragmentName.PLAYLIST_FRAGMENT to R.id.action_recentlyFragment_to_playlistFragment
      ),
      EFragmentName.ALBUM_FRAGMENT to mapOf(
        EFragmentName.HOME_FRAGMENT to R.id.action_albumDetailsFragment_to_homeFragment,
        EFragmentName.PLAYLIST_FRAGMENT to R.id.action_albumDetailsFragment_to_playlistFragment
      )
    )

    fun navigate(id: Int?, bundle: Bundle? = null) {
      if (id == null) return

      navControllerMainActivity?.navigate(id, bundle)
    }
  }

  private var tabLayoutContainerWidth = 0f
  private var maskTabLayoutIconWidth = 0f
  private var currentPosition = 1

  private lateinit var mActivityMainBinding: ActivityMainBinding

  private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    tabLayoutContainerWidth = resources.displayMetrics.widthPixels - 16 * 2 * SCALE_DP_PX

    maskTabLayoutIconWidth = 48 * SCALE_DP_PX

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

    CustomSnackbar.updateActivity(this)

    setUpTabLayoutView()

    mActivityMainBinding.bottomTabLayout.eventListener = this
  }

  private fun setUpTabLayoutView() {
    mActivityMainBinding.bottomTabLayout.apply {

      maskTabLayoutIcon.animate()
        .translationX(currentPosition * (tabLayoutContainerWidth / 4) + (tabLayoutContainerWidth / 4 - maskTabLayoutIconWidth) / 2f)
        .setDuration(300L)
        .start()
    }
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
    }else{
      navControllerMainActivity?.navigateUp()
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

  override fun onGoToFragmentClick(fragmentName: EFragmentName, data: Any?) {
    if (mFragmentName == fragmentName) {
      Toast.makeText(
        applicationContext,
        "${fragmentName.titleFragment} is showing",
        Toast.LENGTH_SHORT
      ).show()
      return
    }

    Log.d("Binh", "Recently fragment: ${mFragmentName.titleFragment}")

    navigate(mapFragmentWithDestinationId[mFragmentName]?.get(fragmentName))

    toggleStateMusicBottomSheet(false)
    when (fragmentName) {
      EFragmentName.LYRIC_FRAGMENT -> {
      }

      else -> {

      }
    }
  }

  override fun onTabClickListener(position: Int) {
    Log.d("Binh", "Tab position: $position")

    if (currentPosition == position) return

    currentPosition = position

    when(position){
      0 ->
        onGoToFragmentClick(EFragmentName.PLAYLIST_FRAGMENT)
      1 ->
        onGoToFragmentClick(EFragmentName.HOME_FRAGMENT)
    }

    setUpTabLayoutView()
  }

  override fun onStart() {
    super.onStart()

    MainApplication.currentActivity = this
  }
}
