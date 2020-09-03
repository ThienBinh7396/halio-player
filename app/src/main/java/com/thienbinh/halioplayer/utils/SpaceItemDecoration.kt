package com.thienbinh.halioplayer.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
  private val spaceBottom: Int,
  private val spaceRight: Int
) :
  RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    outRect.right = spaceRight
    outRect.bottom = spaceBottom
  }
}