package com.example.sololearnclone.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.R
import com.example.sololearnclone.data.Section
import com.example.sololearnclone.adapters.SectionAdapter
import com.example.sololearnclone.databinding.FragmentSectionBinding

class SectionFragment : Fragment() {
    private var binding: FragmentSectionBinding? = null
    private lateinit var adapter: SectionAdapter
    private var sectionList: MutableList<Section> = mutableListOf()
    private lateinit var recyclerViewer: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSectionBinding.inflate(inflater, container, false)

        binding?.run {
            recyclerViewer = recyclerView
        }

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewer.layoutManager = GridLayoutManager(context, 2)
        sectionList = mutableListOf()

        //create SharedPreference to store sections
        val sharedPrefUserInfo = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val phone = sharedPrefUserInfo?.getString("phone", "")
        val sharedPref = activity?.getSharedPreferences(
            "userDoneTask${phone}",
            Context.MODE_PRIVATE
        )

        val editor = sharedPref?.edit()
        if (sharedPref?.all?.isEmpty() == true) {
            editor?.putInt("Introduction", R.drawable.ic_lock_open)
            editor?.putInt("Data Types", R.drawable.ic_lock)
            editor?.putInt("Variables", R.drawable.ic_lock)
            editor?.putInt("Operators", R.drawable.ic_lock)
            editor?.putInt("Input", R.drawable.ic_lock)
        }
        editor?.apply()

        val map = sharedPref?.all
        map?.forEach { (t, any) -> sectionList.add(Section(t, any as Int)) }
        sectionList.reverse()
        adapter = SectionAdapter(requireContext(), sectionList, findNavController())
        recyclerViewer.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}