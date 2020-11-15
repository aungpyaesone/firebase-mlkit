package com.aungpyaesone.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE = 1111
        const val INTENT_TYPE_IMAGE = "image/*"
        const val INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY = 2222
    }

    protected fun selectImageFromGallery() {
        if (isOSLaterThanAndroidM())
            if (isReadStoragePermissionGiven()) pickImageFromGallery() else requestReadExternalStoragePermission()
        else
            pickImageFromGallery()
    }

    // // storage read permission given
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isReadStoragePermissionGiven(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = INTENT_TYPE_IMAGE
        intent.action = Intent.ACTION_GET_CONTENT;
        startActivityForResult(intent, INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY)
    }

    // checkOsVersion
    private fun isOSLaterThanAndroidM(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M
    }

    // requestReadStoragePermission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestReadExternalStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE
        )
    }

    // PremissionGranted
    private fun IntArray.isPermissionGranted(): Boolean {
        return this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isPermissionGranted()) {
                    pickImageFromGallery()
                } else {
                    requestReadExternalStoragePermission()
                }
            }
        }
    }

    // show sanck bar
    protected fun showSnackbar(message: String) {
            Snackbar.make(window.decorView, message, Snackbar.LENGTH_LONG).show()
        }

}