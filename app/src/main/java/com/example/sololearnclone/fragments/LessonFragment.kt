package com.example.sololearnclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.sololearnclone.adapters.ViewPager2Adapter
import com.example.sololearnclone.databinding.FragmentLessonBinding

class LessonFragment : Fragment() {
    private var binding: FragmentLessonBinding? = null
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private var sectionName: String = ""
    private lateinit var progressBar: ProgressBar
    private var lastPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLessonBinding.inflate(inflater, container, false)
        binding?.run {
            progressBar = progressBarTask
            viewPager2 = viewPager
        }

        sectionName = arguments?.getString("lessonName").toString()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = sectionName

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2Adapter = ViewPager2Adapter(requireActivity())
        viewPager2.adapter = viewPager2Adapter
        progressBar.progress = 20

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                if (lastPosition > position) {
                    progressBar.progress -= 20
                } else if (lastPosition < position) {
                    progressBar.progress += 20
                }
                lastPosition = position
            }

            // triggered when there is
            // scroll state will be changed
            override fun onPageScrollStateChanged(state: Int) {
                //to disable swiping in question item
                viewPager2.isUserInputEnabled = viewPager2.currentItem != 2
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}