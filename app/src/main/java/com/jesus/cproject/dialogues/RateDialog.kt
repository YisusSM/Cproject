package com.jesus.cproject.dialogues


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.jesus.cproject.R
import com.jesus.cproject.models.NewRateEvent
import com.jesus.cproject.models.Rate
import com.jesus.cproject.toast
import com.jesus.cproject.utils.RxBus
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class RateDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->

                val textRate = view.editTextRateFeedback.text.toString()
                val imgUrl =
                    FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run { "" }
                val rate = Rate(textRate, view.ratingBarFeedback.rating, Date(), imgUrl)
                RxBus.publish(NewRateEvent(rate))
                activity!!.toast("pressed ok")
            }.setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
                activity!!.toast("pressed cancel")
            }.create()

    }
}