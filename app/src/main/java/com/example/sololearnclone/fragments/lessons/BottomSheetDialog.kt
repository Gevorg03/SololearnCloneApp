package com.example.sololearnclone.fragments.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(
    private val image: Int,
    private val text: String,
    private val btnText: String,
) : BottomSheetDialogFragment() {
    private var binding: FragmentBottomSheetDialogBinding? = null
    private lateinit var imageResult: ImageView
    private lateinit var tvResult: TextView
    private lateinit var btnResult: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetDialogBinding.inflate(
            inflater,
            container,
            false
        )

        binding?.run {
            imageResult = imgResult
            tvResult = textViewResult
            btnResult = buttonResult
        }

        imageResult.setImageResource(image)
        tvResult.text = text
        btnResult.text = btnText

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnResult.setOnClickListener {
            if (btnText == "Continue") {
                val fragment: Fragment? =
                    activity?.supportFragmentManager?.findFragmentById(R.id.fragmnet_container)
                val rootView = fragment?.view
                val viewPager2 = rootView!!.findViewById<ViewPager2>(R.id.view_pager)

                if (viewPager2.currentItem + 1 != 8)
                    viewPager2.currentItem += 1
                else
                    findNavController().navigate(R.id.action_LessonFragment_to_SectionFragment)
            }
            dismiss()
        }
    }
}