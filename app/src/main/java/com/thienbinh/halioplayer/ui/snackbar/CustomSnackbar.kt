package com.thienbinh.halioplayer.ui.snackbar

import android.app.Activity
import android.widget.Button
import androidx.annotation.IdRes
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar
import com.thienbinh.halioplayer.model.CustomToastDataModel

class CustomSnackbar {
  companion object {
    private var mActivity: Activity? = null

    fun updateActivity(activity: Activity?) {
      mActivity = activity
    }

    fun showSnackbar(
      customToastDataModel: CustomToastDataModel,
      colorActionText: Int? = null
    ) {
      if (mActivity == null) return

      val snackbar = Snackbar.make(
        mActivity!!.window.decorView,
        customToastDataModel.message,
        Snackbar.LENGTH_LONG
      )

      snackbar.setAction(customToastDataModel.primaryText) { customToastDataModel.eventListener.onPrimaryButtonClickListener() }

      val button: Button = (snackbar.view).findViewById(R.id.snackbar_action) as Button

      if (colorActionText != null) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
          mActivity?.resources?.getColor(colorActionText, null)?.let { button.setTextColor(it) }
        }else{
          mActivity?.resources?.getColor(colorActionText)?.let { button.setTextColor(it) }
        }
      }

      snackbar.show()
    }
  }
}