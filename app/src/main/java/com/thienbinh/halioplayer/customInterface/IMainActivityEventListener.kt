package com.thienbinh.halioplayer.customInterface

import com.thienbinh.halioplayer.constant.EFragmentName

interface IMainActivityEventListener {
  fun toggleStateMusicBottomSheet(isExpanded: Boolean)
  fun onGoToFragmentClick(fragmentName: EFragmentName, data: Any? = null)
}