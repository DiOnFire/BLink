package me.dion.blink.activity.alerts

import android.app.Activity
import android.app.AlertDialog
import me.dion.blink.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}