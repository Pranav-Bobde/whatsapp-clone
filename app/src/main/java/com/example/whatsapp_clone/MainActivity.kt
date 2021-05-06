package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.whatsapp_clone.adapters.FragmentAdapter
import com.example.whatsapp_clone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager)
        binding.tablayout.setupWithViewPager(binding.viewPager
        )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()

            }
            R.id.logout -> {
                auth.signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            R.id.groupChat -> {
                startActivity(Intent(this, GroupChatActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}