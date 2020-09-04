package com.thienbinh.halioplayer.utils

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener


class RecyclerViewTouchListener(
  context: Context?,
  recycleView: RecyclerView,
  private val clickListener: ClickListener?
) :
  OnItemTouchListener {
  interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
  }

  private val gestureDetector: GestureDetector
  override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
    val child: View? = rv.findChildViewUnder(e.x, e.y)
    if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
      clickListener.onClick(child, rv.getChildAdapterPosition(child))
    }
    return false
  }

  override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
  override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

  init {
    gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
      override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
      }

      override fun onLongPress(e: MotionEvent) {
        val child: View? = recycleView.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null) {
          clickListener.onLongClick(child, recycleView.getChildAdapterPosition(child))
        }
      }
    })
  }
}