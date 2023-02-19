package com.example.sololearnclone.fragments.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.R
import com.example.sololearnclone.adapters.MovingItemsAdapter
import com.example.sololearnclone.databinding.FragmentMovingItemsTaskBinding

class MovingItemsTaskFragment : Fragment() {
    private var binding: FragmentMovingItemsTaskBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovingItemsAdapter
    private var list: List<String> = listOf()
    private lateinit var btnCheck: Button
    private var questionList = listOf(
        "package main", "class Main {", "\t\tfun main(args: Array<String>) {",
        "\t\t\t\t\t\t println(\"Hello World\")", "\t\t}", "}",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovingItemsTaskBinding.inflate(inflater, container, false)

        binding?.run {
            this@MovingItemsTaskFragment.recyclerView = recyclerView
            this@MovingItemsTaskFragment.btnCheck = btnCheck
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = arrayListOf()
        list = questionList.shuffled()

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MovingItemsAdapter(list)

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter

        btnCheck.setOnClickListener {
            val bottomSheetFragment: BottomSheetDialog =
                if (questionList == adapter.getResultList()) {
                    BottomSheetDialog(
                        R.drawable.ic_green_check,
                        "Good job!",
                        "Continue"
                    )
                } else {
                    BottomSheetDialog(
                        R.drawable.ic_cancel,
                        "Hmm, think again",
                        "Try Again"
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
}