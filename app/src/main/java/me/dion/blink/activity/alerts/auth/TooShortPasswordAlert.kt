package me.dion.blink.activity.alerts.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class TooShortPasswordAlert : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle("Password too short!")
                .setMessage("Your password must contain 8 or more symbols.")
                .setPositiveButton("Ok") {
                        dialog, _ -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("empty activity")
    }
}