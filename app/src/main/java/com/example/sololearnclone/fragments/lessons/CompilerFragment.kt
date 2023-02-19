package com.example.sololearnclone.fragments.lessons

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import com.amrdeveloper.codeview.CodeView
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentCompilerBinding
import java.util.regex.Pattern

class CompilerFragment : Fragment() {
    private var binding: FragmentCompilerBinding? = null
    private lateinit var codeView: CodeView
    private lateinit var buttonCheck: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCompilerBinding.inflate(inflater, container, false)
        binding?.run {
            buttonCheck = btnCheck
            this@CompilerFragment.codeView = codeView
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCheck.setOnClickListener {
            val bottomSheetFragment = BottomSheetDialog(
                R.drawable.ic_green_check,
                "You are always right",
                "Continue"
            )
            activity?.supportFragmentManager?.let { it1 ->
                bottomSheetFragment.show(
                    it1,
                    bottomSheetFragment.tag
                )
            }
        }

        codeView.setEnableLineNumber(true)
        codeView.setLineNumberTextSize(45f)
        codeView.setLineNumberTextColor(resources.getColor(R.color.dark_orange, null))

        codeView.enablePairComplete(true)
        codeView.enablePairCompleteCenterCursor(true)

        val indentationStart: MutableSet<Char> = mutableSetOf('{')
        codeView.setIndentationStarts(indentationStart)
        val indentationEnds: MutableSet<Char> = mutableSetOf('}')
        codeView.setIndentationEnds(indentationEnds)
        codeView.setTabLength(4)
        codeView.setEnableAutoIndentation(true)
        codeView.setMatchingHighlightColor(Color.YELLOW)


        val pairCompleteMap: MutableMap<Char, Char> =
            hashMapOf('{' to '}', '[' to ']', '(' to ')', '<' to '>', '"' to '"', '\'' to '\'')

        codeView.setPairCompleteMap(pairCompleteMap)

        val mainPackageBody = "package main" +
                "\n\nclass Main {" +
                "\n\t\tfun main(args: Array<String>) {" +
                "\n\t\t\t\t\t\t" +
                "\n\t\t}" +
                "\n}"

        val colorMap =
            KotlinKeywords.values().associate { Pattern.compile(it.name.lowercase()) to it.color }

        codeView.setSyntaxPatternsMap(colorMap)
        codeView.resetHighlighter()
        codeView.setTextHighlighted(mainPackageBody)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}