package com.jesus.cproject.dialogues


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.jesus.cproject.R
import com.jesus.cproject.toast

class RateDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, wich ->
                activity!!.toast("pressed ok")

            }.setNegativeButton(getString(R.string.dialog_cancel)) { dialog, wich ->
                activity!!.toast("pressed cancel")
            }.create()

    }
}