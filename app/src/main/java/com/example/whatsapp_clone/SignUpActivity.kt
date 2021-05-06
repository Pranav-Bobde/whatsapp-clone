package com.example.whatsapp_clone

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp_clone.databinding.ActivitySignUpBinding
import com.example.whatsapp_clone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creating Account")
        progressDialog.setMessage("We're Creating Your Account!")

        binding.btnSignUp.setOnClickListener {
            progressDialog.show()
            auth.createUserWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
                .addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val user: Users = Users(
                            binding.etUserName.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString()
                        )

                        val id = task.result?.user?.uid
                        database.reference.child("Users").child(id!!).setValue(user)
                        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }
}