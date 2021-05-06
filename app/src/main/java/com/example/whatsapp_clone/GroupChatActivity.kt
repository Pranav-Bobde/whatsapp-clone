package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp_clone.adapters.ChatAdapter
import com.example.whatsapp_clone.databinding.ActivityGroupChatActivityBinding
import com.example.whatsapp_clone.models.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatActivityBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var auth: FirebaseAuth
    private var messages = ArrayList<Messages>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        chatAdapter = ChatAdapter(messages, this)


        //Getting Sender's Id
        val senderId = auth.uid

        //Setting UserName for group chat
        binding.userName.text = "Group Chat"

        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        //Getting/Reading data from firebase database
        database.reference.child("Group Chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        val message = dataSnapshot.getValue(Messages::class.java)
                        messages.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //Sending data to firebase database
        binding.send.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val date = Date()
            val messages: Messages = Messages(senderId!!, message, date.time)
            binding.etMessage.text.clear()

            database.reference.child("Group Chat")
                .push()
                .setValue(messages).addOnSuccessListener {

                }
        }


    }
}