package com.jesus.cproject.fragments

import android.net.sip.SipSession
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.jesus.cproject.R
import com.jesus.cproject.loadByResource
import com.jesus.cproject.loadByUrl
import com.jesus.cproject.toast
import com.jesus.cproject.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_item_left.*
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import java.util.*
import java.util.EventListener

class InfoFragment : Fragment() {
    private lateinit var _view: View
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDatabaseReference: CollectionReference

    private val chatSubscription: ListenerRegistration? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_info, container, false)

        setUpChatDatabase()
        currentUser()
        setUpCurrentUserInformation()

        //Total Messages Firebase Style
        subscribeToTotalMessagesFirebaseStyle()


        return _view
    }

    private fun setUpChatDatabase() {
        chatDatabaseReference = store.collection("chat")
    }

    private fun currentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInformation() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let { it }
            ?.run { getString(R.string.info_no_name) }
        currentUser.photoUrl?.let {
            Picasso.get().load(currentUser.photoUrl).resize(300, 300).centerCrop()
                .transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        }?.run {
            Picasso.get().load(R.drawable.ic_person).resize(300, 300).centerCrop()
                .transform(CircleTransform()).into(_view.imageViewInfoAvatar)
        }


    }

    private fun subscribeToTotalMessagesFirebaseStyle(){
        chatDatabaseReference.addSnapshotListener(object : EventListener,
            com.google.firebase.firestore.EventListener<QuerySnapshot>{
            override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("Exception $it")
                    return
                }
                querySnapshot?.let { _view.textViewInfoTotalMessages.text = it.size().toString()}
            }

        })
    }

}