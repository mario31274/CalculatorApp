package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return KeypadFragment.create(1)
        if (position == 1)
            return KeypadFragment.create(2)
        throw Exception("Create KeypadFragment Error position: $position")
    }
}