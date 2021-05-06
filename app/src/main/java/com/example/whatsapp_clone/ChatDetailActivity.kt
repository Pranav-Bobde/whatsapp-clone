package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp_clone.adapters.ChatAdapter
import com.example.whatsapp_clone.databinding.ActivityChatDetailBinding
import com.example.whatsapp_clone.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val senderId = auth.uid
        val receiverId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profilePic = intent.getStringExtra("profilePic")

        binding.userName.text = userName
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_avatar).into(binding.profileImage)

        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val messages = ArrayList<Messages>()
        val chatAdapter = ChatAdapter(messages, this, receiverId!!)

        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        //Sender and Reciever Id Rooms
        val senderRoom = senderId + receiverId
        val recieverRoom = receiverId + senderId

        //getting data from database
        database.reference.child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear() //to avoid repeated messages
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val message = dataSnapshot.getValue(Messages::class.java)
                        message!!.messageId =  dataSnapshot.key
                        messages.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged() //otherwise had to press back button for sender to see the message
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        binding.send.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val date = Date()
            val messages: Messages = Messages(senderId!!, message, date.time)
            binding.etMessage.text.clear()

            //sending data to databse
            //sending data in sender's room
            database.reference.child("chats")
                .child(senderRoom)
                .push()
                .setValue(messages)
                .addOnSuccessListener {
                    //sending data in reciever's room
                    database.reference.child("chats")
                        .child(recieverRoom)
                        .push()
                        .setValue(messages)
                }

        }


    }
}