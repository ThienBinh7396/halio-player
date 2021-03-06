package com.thienbinh.halioplayer.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.thienbinh.halioplayer.store
import com.thienbinh.halioplayer.store.action.PermissionAction


class RequestPermissionRuntime {
  companion object {
    val REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 123

    fun checkPermissionReadExternalStorage(context: Context): Boolean {
      var checkPermission = false

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
          ) != PackageManager.PERMISSION_GRANTED
        ) {
          if (ActivityCompat.shouldShowRequestPermissionRationale(
              context as Activity,
              Manifest.permission.READ_EXTERNAL_STORAGE
            )
          ) {
            showRequestPermissonDialog(
              "Read external storage", context,
              Manifest.permission.READ_EXTERNAL_STORAGE
            );
          } else {
            ActivityCompat.requestPermissions(
              context,
              arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
              REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
            )
          }

        } else {
          checkPermission = true
        }

      } else {
        checkPermission = true
      }

      store.dispatch(
        PermissionAction.PERMISSION_ACTION_UPDATE_READ_EXTERNAL_STORAGE_PERMISSION(
          checkPermission
        )
      )

      return checkPermission
    }

    private fun showRequestPermissonDialog(
      msg: String,
      context: Activity,
      readExternalStorage: String
    ) {
      val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
      alertBuilder.setCancelable(true)
      alertBuilder.setTitle("Permission necessary")
      alertBuilder.setMessage("$msg permission is necessary")
      alertBuilder.setPositiveButton(
        "YES"
      ) { _, _ ->
        ActivityCompat.requestPermissions(
          context, arrayOf(readExternalStorage),
          REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
        )
      }
      val alert: AlertDialog = alertBuilder.create()
      alert.show()
    }

  }
}