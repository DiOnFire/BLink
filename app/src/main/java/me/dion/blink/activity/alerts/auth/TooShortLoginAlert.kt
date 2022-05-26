package me.dion.blink.activity.alerts.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class TooShortLoginAlert : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle("Login too short!")
                .setMessage("Your login must contain 3 or more symbols")
                .setPositiveButton("Ok") {
                        dialog, _ -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("empty activity")
    }
}