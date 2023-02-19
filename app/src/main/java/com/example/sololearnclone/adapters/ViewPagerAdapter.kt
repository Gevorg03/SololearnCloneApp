package com.example.sololearnclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sololearnclone.fragments.lessons.*

class ViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0,1 -> LessonOneFragment()
            2 -> TaskOneFragment()
            3 -> MovingWordsTaskFragment()
            4 -> LessonOneFragment()
            5 -> CompilerFragment()
            6 -> MovingItemsTaskFragment()
            else -> TaskTwoFragment()
        }
    }

    override fun getItemCount(): Int {
        return 8
    }
}