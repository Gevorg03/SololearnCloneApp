package com.example.sololearnclone.fragments.lessons

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.sololearnclone.R
import com.example.sololearnclone.data.Section
import com.example.sololearnclone.databinding.FragmentTaskTwoBinding
import com.example.sololearnclone.fragments.HomeFragment

class TaskTwoFragment : Fragment() {
    private var binding: FragmentTaskTwoBinding? = null
    private lateinit var buttonCheck: Button
    private var listCheckBox: MutableList<CheckBox> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskTwoBinding.inflate(inflater, container, false)

        binding?.run {
            buttonCheck = btnCheck
            listCheckBox.addAll(
                mutableListOf(
                    checkboxJava,
                    checkboxKotlin,
                    checkboxPython,
                    checkboxCpp,
                    checkboxJs
                )
            )
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCheck.setOnClickListener {
            if (listCheckBox[0].isChecked && listCheckBox[1].isChecked && listCheckBox[2].isChecked
                && listCheckBox[3].isChecked && listCheckBox[4].isChecked
            ) {
                println(HomeFragment.phone_)
                val sharedPref = activity?.getSharedPreferences(
                    "userDoneTask${HomeFragment.phone_}",
                    Context.MODE_PRIVATE
                )
                val editor = sharedPref?.edit()
                val actionBar = (activity as AppCompatActivity).supportActionBar
                val section = actionBar?.title.toString()
                val map = sharedPref?.all
                val lst: MutableList<Section> = mutableListOf()
                map?.forEach { (t, any) -> lst.add(Section(t, any as Int)) }
                lst.reverse()
                val curr = lst.indexOf(Section(section, R.drawable.ic_lock_open))
                if (curr + 1 != lst.size) {
                    editor?.putInt(lst[curr + 1].sectionName, R.drawable.ic_lock_open)
                }
                editor?.putInt(section, R.drawable.ic_green_check)
                editor?.apply()
                Toast.makeText(
                    requireContext(),
                    "Successfully you finished $section",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_LessonFragment_to_SectionFragment)
            } else {
                Toast.makeText(requireContext(), "Wrong answer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}