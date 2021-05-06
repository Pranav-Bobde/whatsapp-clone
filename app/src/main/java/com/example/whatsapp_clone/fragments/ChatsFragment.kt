package com.example.whatsapp_clone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.adapters.UsersAdapter
import com.example.whatsapp_clone.databinding.FragmentChatsBinding
import com.example.whatsapp_clone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var database: FirebaseDatabase
    private var list = ArrayList<Users>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        val adapter = UsersAdapter(list, context!!)
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)

        database = FirebaseDatabase.getInstance()

        //To get the list of Users from the Firebase Database
        database.reference.child("Users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear() // #To avoid Error (IMPORTANT TO NOTE)
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val users = dataSnapshot.getValue(Users::class.java)
                    users?.userId = dataSnapshot.key
                    if (!users!!.userId.equals(FirebaseAuth.getInstance().uid))
                        list.add(users)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }

}