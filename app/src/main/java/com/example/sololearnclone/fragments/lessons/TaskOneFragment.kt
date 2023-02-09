package com.example.sololearnclone.fragments.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentTaskOneBinding


class TaskOneFragment : Fragment() {
    private var binding: FragmentTaskOneBinding? = null
    private lateinit var radioGroup: RadioGroup
    private lateinit var butotnCheck: Button
    private lateinit var viewLesson: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskOneBinding.inflate(inflater, container, false)
        viewLesson = inflater.inflate(R.layout.fragment_lesson, null)

        binding?.run {
            radioGroup = radioGroupCheck
            butotnCheck = btnCheck
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        butotnCheck.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "No answer has been selected",
                    Toast.LENGTH_SHORT).show()
            } else {
                val radioButton = radioGroup
                    .findViewById<View>(selectedId) as RadioButton
                if (radioButton.text.toString() == "True") {
                    val fragment: Fragment? =
                        activity?.supportFragmentManager?.findFragmentById(R.id.fragmnet_container)
                    val rootView = fragment?.view
                    val viewPager2 = rootView!!.findViewById<ViewPager2>(R.id.view_pager)

                    viewPager2.currentItem += 1
                } else {
                    Toast.makeText(requireContext(), "Wrong answer",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}