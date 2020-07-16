package com.jesus.cproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.jesus.cproject.R
import com.jesus.cproject.adapters.ChatAdapter
import com.jesus.cproject.models.Message
import com.jesus.cproject.toast
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    private lateinit var _view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatDatabaseReference: CollectionReference

    private var chatSubscription:ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_chat, container, false)
        setUpChatDatabase()
        currentUser()
        setUpRecyclerView()
        setUpChatButton()
        subscribeToChatMessage()
        return _view
    }

    private fun setUpChatButton() {
        _view.btnSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() }?: run {""}
                val message = Message(
                    currentUser.uid, messageText, photo,
                    Date()
                )
                //guardamos el mensaje en firebase
                saveMessage(message)
                _view.editTextMessage.text.clear()
            }
        }
    }

    private fun setUpChatDatabase() {
        chatDatabaseReference = store.collection("chat")
    }

    private fun currentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter

    }

    private fun saveMessage(message: Message) {
        val newMessage = HashMap<String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageURL"] = message.profileImageURL
        newMessage["sentAt"] = message.sentAt

        chatDatabaseReference.add(newMessage).addOnCompleteListener {
            activity!!.toast("Message Added")
        }.addOnFailureListener {
            activity!!.toast("Message Error, Try Again")
        }
    }

    private fun subscribeToChatMessage() {
       chatSubscription = chatDatabaseReference.orderBy("sentAt",Query.Direction.ASCENDING).addSnapshotListener(object : EventListener,
            com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("exception! $it")
                    return
                }
                snapshot?.let {
                    messageList.clear()
                    val messages = it.toObjects(Message::class.java)
                    messageList.addAll(messages)
                    adapter.notifyDataSetChanged()
                    _view.recyclerView.smoothScrollToPosition(messageList.size)
                }
            }

        })
    }

    override fun onDestroy() {
        chatSubscription?.remove()
        super.onDestroy()

    }

}