package com.example.sololearnclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.sololearnclone.viewModels.ProgressBarViewModel
import com.example.sololearnclone.adapters.ViewPager2Adapter
import com.example.sololearnclone.databinding.FragmentLessonBinding
import kotlinx.coroutines.flow.collectLatest

class LessonFragment : Fragment() {
    private var binding: FragmentLessonBinding? = null
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private var sectionName: String = ""
    private lateinit var progressBar: ProgressBar
    private val viewModel: ProgressBarViewModel by activityViewModels()

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
        viewModel.progress.value = 15
        viewModel.lastPosition.value = 0
        progressBar.progress = viewModel.progress.value

        binding?.run {
            lifecycleScope.launchWhenCreated {
                viewModel.progress.collectLatest { progress ->
                    progressBar.progress = progress
                }
            }

        }

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                if (viewModel.lastPosition.value > position) {
                    viewModel.progress.value -= 100 / 8
                    println(viewModel.progress.value)
                } else if (viewModel.lastPosition.value < position) {
                    viewModel.progress.value += 100 / 8
                }
                viewModel.lastPosition.value = position
            }

            // triggered when there is
            // scroll state will be changed
            override fun onPageScrollStateChanged(state: Int) {
                //to disable swiping in question item
                viewPager2.isUserInputEnabled =
                    !(viewPager2.currentItem == 2
                            || viewPager2.currentItem == 3
                            || viewPager2.currentItem == 6)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}