package me.dion.blink.activity.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class AbstractAlert(val title: String, val message: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok") {
                        dialog, _ -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("empty activity")
    }
}
