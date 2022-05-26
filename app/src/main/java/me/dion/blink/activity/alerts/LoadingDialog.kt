package me.dion.blink.activity.alerts

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(val activity: Activity) {
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
    }
}