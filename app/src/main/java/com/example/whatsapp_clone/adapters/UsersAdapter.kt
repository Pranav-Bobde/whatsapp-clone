package com.example.whatsapp_clone.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp_clone.ChatDetailActivity
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UsersAdapter(private var list: ArrayList<Users>, var context:Context): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.sample_show_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        Picasso.get().load(user.profilepic).placeholder(R.drawable.ic_avatar).into(holder.image)
        holder.userName.text = user.userName

        FirebaseDatabase.getInstance().reference.child("chats")
            .child(FirebaseAuth.getInstance().uid + user.userId)
            .orderByChild("timeStamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            holder.lastMessage.text =
                                dataSnapshot.child("message").value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatDetailActivity::class.java).apply {
                putExtra("userId", user.userId)
                putExtra("profilePic", user.profilepic)
                putExtra("userName", user.userName)
            }
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        var image: ImageView = item.findViewById(R.id.profile_image)
        var userName: TextView = item.findViewById(R.id.userNameList)
        var lastMessage: TextView = item.findViewById(R.id.lastMessage)
    }
}