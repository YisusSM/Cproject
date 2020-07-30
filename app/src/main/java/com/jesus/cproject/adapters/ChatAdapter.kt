package com.jesus.cproject.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesus.cproject.R
import com.jesus.cproject.inflate
import com.jesus.cproject.loadByResource
import com.jesus.cproject.loadByUrl
import com.jesus.cproject.models.Message
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import java.text.SimpleDateFormat

class ChatAdapter(val item: List<Message>, val userId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_MESSAGE -> ViewHolderR(parent.inflate(layoutRight))
            else -> ViewHolderL(parent.inflate(layoutLeft))
        }
    }

    override fun getItemCount() = item.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            MY_MESSAGE -> (holder as ViewHolderR).bind(item[position])
            else -> (holder as ViewHolderL).bind(item[position])
        }
    }

    override fun getItemViewType(position: Int) =
        if (item[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE

    class ViewHolderR(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) = with(itemView) {
            textViewMessageRight.text = message.message
            textViewTimeRight.text = SimpleDateFormat("hh:mm:ss").format(message.sentAt)
            //Picasso load image here
            if (message.profileImageURL.isEmpty()) {
                imageViewProfileRight.loadByResource(R.drawable.ic_person)
            } else {
                imageViewProfileRight.loadByUrl(message.profileImageURL)
            }

        }
    }

    class ViewHolderL(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) = with(itemView) {
            textViewMessageLeft.text = message.message
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            //Picasso load image here
            if (message.profileImageURL.isNotEmpty()) {
                imageViewProfileLeft.loadByUrl(message.profileImageURL)
            } else {
                imageViewProfileLeft.loadByResource(R.drawable.ic_person)
            }
        }
    }
}