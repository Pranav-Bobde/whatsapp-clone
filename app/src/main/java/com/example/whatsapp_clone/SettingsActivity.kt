package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp_clone.databinding.ActivitySettingsBinding
import com.example.whatsapp_clone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        //backArrow funtion
        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        //profilePic function
        binding.plus.setOnClickListener {
            val intent = Intent()
            intent.apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                startActivityForResult(intent, 33)
            }
        }

        //storing data from storage to database and UPDATING SETTINGS XML
        database.reference.child("Users")
            .child(auth.uid!!)
            .addListenerForSingleValueEvent( object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    //UPDATING DP
                    Picasso.get().load(user!!.profilepic)
                        .placeholder(R.drawable.ic_avatar)
                        .into(binding.profileImage)

                    //UPDATING USERNAME
                    binding.etUserName.setText(user.userName)
                    //UPDATING STATUS
                    binding.etStatus.setText(user.status)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //SAVE button
        binding.saveButton.setOnClickListener {
            val status = binding.etStatus.text
            val userName = binding.etUserName.text

            val obj = HashMap<String, Any>()
            obj.put("userName", userName)
            obj.put("status", status)

            //update user data i.e. username and status
            database.reference.child("Users")
                .child(auth.uid!!)
                .updateChildren(obj)

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()

        }

    }

    //result from opening photo picker for dp
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.data != null) { //checks if user selected a pick or not
            val sFile = data.data //Uri of photo
            binding.profileImage.setImageURI(sFile)

            val reference = storage.reference.child("profile_pictures")
                .child(auth.uid!!)

            reference.putFile(sFile!!).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener {
                    database.reference.child("Users")
                        .child(auth.uid!!)
                        .child("profilepic")
                        .setValue(it.toString())

                    Toast.makeText(this, "Profile Pic Updated!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}