package com.thienbinh.halioplayer.ui.snackbar

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.thienbinh.halioplayer.model.CustomToastDataModel

class CustomSnackbar {
  companion object {
    private var mActivity: Activity? = null

    fun updateActivity(activity: Activity?) {
      mActivity = activity
    }

    fun showSnackbar(customToastDataModel: CustomToastDataModel) {
      if (mActivity == null) return

      val snackbar = Snackbar.make(
        mActivity!!.window.decorView,
        customToastDataModel.message,
        Snackbar.LENGTH_LONG
      )

      snackbar.setAction(customToastDataModel.primaryText) { customToastDataModel.eventListener.onPrimaryButtonClickListener() }

      snackbar.show()
    }
  }
}