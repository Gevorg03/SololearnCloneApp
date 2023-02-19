package com.example.sololearnclone.fragments.lessons

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.FragmentMovingWordsTaskBinding

class MovingWordsTaskFragment : Fragment(), View.OnTouchListener, View.OnDragListener {
    private var binding: FragmentMovingWordsTaskBinding? = null
    private val logcat: String? = null
    private lateinit var tvFirstAnswer: TextView
    private lateinit var tvSecondAnswer: TextView
    private lateinit var layoutWords: LinearLayout
    private lateinit var layoutFirstAnswer: LinearLayout
    private lateinit var layoutSecondAnswer: LinearLayout
    private lateinit var layoutOtherPart: LinearLayout
    private lateinit var buttonCheck: Button
    private lateinit var firstAnswer: View
    private lateinit var secondAnswer: View
    private val firstAnswerEmpty = "               "
    private val secondAnswerEmpty = "                        "

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovingWordsTaskBinding.inflate(inflater, container, false)

        binding?.run {
            tvFirstAnswer = textViewFirstAnswer
            tvSecondAnswer = textViewSecondAnswer
            tvFirstAnswer.paintFlags = tvFirstAnswer.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            tvSecondAnswer.paintFlags = tvSecondAnswer.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            buttonCheck = btnCheck
            this@MovingWordsTaskFragment.layoutFirstAnswer = layoutFirstAnswer
            this@MovingWordsTaskFragment.layoutSecondAnswer = layoutSecondAnswer
            this@MovingWordsTaskFragment.layoutWords = layoutWords
            this@MovingWordsTaskFragment.layoutOtherPart = layoutOtherPart
            tvOut.setOnTouchListener(this@MovingWordsTaskFragment)
            tvScreen.setOnTouchListener(this@MovingWordsTaskFragment)
            tvPrint.setOnTouchListener(this@MovingWordsTaskFragment)
            tvSystem.setOnTouchListener(this@MovingWordsTaskFragment)
            tvKotlin.setOnTouchListener(this@MovingWordsTaskFragment)

            layoutFirstAnswer.setOnDragListener(this@MovingWordsTaskFragment)
            layoutSecondAnswer.setOnDragListener(this@MovingWordsTaskFragment)
            layoutWords.setOnDragListener(this@MovingWordsTaskFragment)
            layoutOtherPart.setOnDragListener(this@MovingWordsTaskFragment)
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bottomSheetFragment: BottomSheetDialog


        buttonCheck.setOnClickListener {
            var firstText = ""
            var secondText = ""

            if (this::firstAnswer.isInitialized)
                firstText = (firstAnswer as TextView).text.toString()
            if (this::secondAnswer.isInitialized)
                secondText = (secondAnswer as TextView).text.toString()

            if (tvFirstAnswer.text.toString() == firstAnswerEmpty
                || tvSecondAnswer.text.toString() == secondAnswerEmpty
            ) {
                bottomSheetFragment =
                    BottomSheetDialog(
                        R.drawable.ic_cancel,
                        "Fill in all fields",
                        "Try Again"
                    )
            } else if (firstText == "println" && secondText == "Kotlin is fun"
            ) {
                bottomSheetFragment =
                    BottomSheetDialog(
                        R.drawable.ic_green_check,
                        "Good job!",
                        "Continue"
                    )
            } else {
                bottomSheetFragment =
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            val shadowBuilder = View.DragShadowBuilder(v)
            v?.startDragAndDrop(null, shadowBuilder, v, 0)
            v?.visibility = View.INVISIBLE
            true
        } else {
            false
        }
    }


    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(logcat, "Drag event started")
            }

            DragEvent.ACTION_DRAG_ENTERED -> Log.d(
                logcat,
                "Drag event entered into " + v.toString()

            )

            DragEvent.ACTION_DRAG_EXITED -> Log.d(
                logcat,
                "Drag event exited from " + v.toString()
            )

            DragEvent.ACTION_DROP -> {
                Log.d(logcat, "Dropped")
                val view = event.localState as View
                val owner = view.parent as ViewGroup
                var container = v as LinearLayout
                if (!(owner == layoutWords && container == layoutWords)
                    && !(owner == layoutWords && container == layoutOtherPart)
                ) {
                    owner.removeView(view)
                    if (container == layoutOtherPart)
                        container = layoutWords
                    container.addView(view)

                    if (container == layoutFirstAnswer) {
                        changeTwoWords(tvFirstAnswer, firstAnswer, container, owner)
                        if (tvSecondAnswer.text == "" && owner == layoutSecondAnswer) {
                            if (tvFirstAnswer.text == firstAnswerEmpty)
                                tvSecondAnswer.text = secondAnswerEmpty
                            else {
                                container.removeView(firstAnswer)
                                owner.addView(firstAnswer)
                                val tmp = secondAnswer
                                secondAnswer = firstAnswer
                                firstAnswer = tmp
                            }
                        }
                        firstAnswer = view
                        tvFirstAnswer.text = ""
                    } else if (container == layoutSecondAnswer) {
                        changeTwoWords(tvSecondAnswer, secondAnswer, container, owner)
                        if (tvFirstAnswer.text == "" && owner == layoutFirstAnswer) {
                            if (tvSecondAnswer.text == secondAnswerEmpty)
                                tvFirstAnswer.text = firstAnswerEmpty
                            else {
                                container.removeView(secondAnswer)
                                owner.addView(secondAnswer)
                                val tmp = secondAnswer
                                secondAnswer = firstAnswer
                                firstAnswer = tmp
                            }
                        }
                        secondAnswer = view
                        tvSecondAnswer.text = ""
                    } else {
                        if (tvFirstAnswer.text == "" && owner == layoutFirstAnswer)
                            tvFirstAnswer.text = firstAnswerEmpty
                        if (tvSecondAnswer.text == "" && owner == layoutSecondAnswer)
                            tvSecondAnswer.text = secondAnswerEmpty
                    }
                    view.visibility = View.VISIBLE
                } else {
                    if (tvFirstAnswer.text != "" && container != layoutOtherPart) {
                        tvFirstAnswer.text = ""
                        owner.removeView(view)
                        container = layoutFirstAnswer
                        container.addView(view)
                        firstAnswer = view
                    } else if (tvSecondAnswer.text != "" && container != layoutOtherPart) {
                        tvSecondAnswer.text = ""
                        owner.removeView(view)
                        container = layoutSecondAnswer
                        container.addView(view)
                        secondAnswer = view
                    }
                }
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(logcat, "Drag ended")
                val view = event.localState as View
                val container = v as LinearLayout
                if (container != layoutFirstAnswer
                    && container != layoutSecondAnswer
                ) {
                    view.visibility = View.VISIBLE
                }
            }

            else -> {}
        }
        return true
    }

    private fun changeTwoWords(tvAnswer: TextView, answer: View, container: LinearLayout, owner: ViewGroup) {
        //Change, if container has a word
        if (tvAnswer.text == "" && owner == layoutWords) {
            container.removeView(answer)
            owner.addView(answer)
        }
    }
}