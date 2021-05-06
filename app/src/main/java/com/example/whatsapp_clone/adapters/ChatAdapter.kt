package com.example.whatsapp_clone.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp_clone.GroupChatActivity
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
//, var recId: String
class ChatAdapter(private var messages: ArrayList<Messages>, var context: Context, var recId: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    constructor(messages: java.util.ArrayList<Messages>, context: GroupChatActivity) :
            this(
                messages,
                context,
                null
            )

    class RecieverViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var recieverMessage: TextView = item.findViewById(R.id.recieverText)
        var recieverTime: TextView = item.findViewById(R.id.recieverTime)
    }

    class SenderViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var senderMessage: TextView = item.findViewById(R.id.senderText)
        var senderTime: TextView = item.findViewById(R.id.senderTime)
    }

    private val SENDER_VIEW_TYPE = 1
    private val RECIEVER_VIEW_TYPE = 2

    override fun getItemViewType(position: Int): Int {
        if (messages.get(position).uid.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                return SENDER_VIEW_TYPE
        }
        else {
            return RECIEVER_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENDER_VIEW_TYPE) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.sample_sender, parent, false)
            return SenderViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.sample_reciever, parent, false)
            return RecieverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        //Delete fun - problem for grp chat
        holder.itemView.setOnLongClickListener {
            val obj = AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes") { _, _ ->
                    val database = FirebaseDatabase.getInstance()
                    val senderRoom = FirebaseAuth.getInstance().uid + recId
                    database.reference.child("chats")
                        .child(senderRoom)
                        .child(message.messageId!!)
                        .setValue(null)
                }
                .setNegativeButton("no") { _, _ ->
                }
            return@setOnLongClickListener true
        }
        if (holder::class.java == SenderViewHolder::class.java) {
            (holder as SenderViewHolder).senderMessage.text = message.message
        }
        else {
            (holder as RecieverViewHolder).recieverMessage.text = message.message
        }
    }

    override fun getItemCount(): Int = messages.size


}
