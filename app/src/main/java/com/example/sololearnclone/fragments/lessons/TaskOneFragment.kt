package com.example.sololearnclone.fragments.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentTaskOneBinding

class TaskOneFragment : Fragment() {
    private var binding: FragmentTaskOneBinding? = null
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonCheck: Button
    private lateinit var viewLesson: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskOneBinding.inflate(inflater, container, false)
        viewLesson = inflater.inflate(R.layout.fragment_lesson, null)

        binding?.run {
            radioGroup = radioGroupCheck
            buttonCheck = btnCheck
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bottomSheetFragment: BottomSheetDialog

        buttonCheck.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val radioButton: RadioButton? = radioGroup
                .findViewById<View>(selectedId) as? RadioButton
            val answer = radioButton?.text.toString()

            if (selectedId == -1) {
                bottomSheetFragment =
                    BottomSheetDialog(
                        R.drawable.ic_cancel,
                        "Choose an answer",
                        "Try Again"
                    )
            } else if (answer == "False") {
                bottomSheetFragment =
                    BottomSheetDialog(
                        R.drawable.ic_cancel,
                        "Hmm, think again",
                        "Try Again"
                    )
            } else {
                bottomSheetFragment =
                    BottomSheetDialog(
                        R.drawable.ic_green_check,
                        "Good job!",
                        "Continue"
                    )
            }
            activity?.supportFragmentManager?.let { it1 ->
                bottomSheetFragment.show(
                    it1,
                    bottomSheetFragment.tag
                )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}