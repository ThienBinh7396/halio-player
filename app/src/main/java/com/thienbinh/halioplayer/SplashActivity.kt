package com.thienbinh.halioplayer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.thienbinh.halioplayer.store.action.PermissionAction
import com.thienbinh.halioplayer.utils.FirstActionInitializeData
import com.thienbinh.halioplayer.utils.RequestPermissionRuntime


class SplashActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    overridePendingTransition(R.anim.enter_slide_right_anim, R.anim.exit_slide_left_anim)

    val requiredPermission = arrayListOf(
      RequestPermissionRuntime.checkPermissionReadExternalStorage(this)
    )

    FirstActionInitializeData.initialize(this)

    if (requiredPermission.find { !it } == null) {
      FirstActionInitializeData.loadMusicFromDevice(this)

      goToMainActivity()
    }
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

        FirstActionInitializeData.loadMusicFromDevice(this)
      } else {
        Toast.makeText(
          this, "Read external storage permission denied",
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


    goToMainActivity()
  }

  private fun goToMainActivity() {
    Handler(Looper.getMainLooper()).postDelayed({
      val intent = Intent(this@SplashActivity, MainActivity::class.java)
      startActivity(intent)
      finish()
    }, 1000)
  }
}