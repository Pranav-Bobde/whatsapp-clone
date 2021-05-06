package com.example.whatsapp_clone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp_clone.fragments.CallsFragment
import com.example.whatsapp_clone.fragments.ChatsFragment
import com.example.whatsapp_clone.fragments.StatusFragment

class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {return ChatsFragment()}
            1 -> {return StatusFragment()}
            2 -> {return CallsFragment()}
        }
        return ChatsFragment()
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when(position) {
            0 -> {title = "CHATS"}
            1 -> {title = "STATUS"}
            2 -> {title = "CALLS"}
        }
        return title
    }
}