package com.example.sololearnclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sololearnclone.fragments.lessons.LessonOneFragment
import com.example.sololearnclone.fragments.lessons.TaskOneFragment
import com.example.sololearnclone.fragments.lessons.TaskTwoFragment

class ViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0,1 -> LessonOneFragment()
            2 -> TaskOneFragment()
            3 -> LessonOneFragment()
            else -> TaskTwoFragment()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}