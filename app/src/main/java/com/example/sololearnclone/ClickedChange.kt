package com.example.sololearnclone

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface ClickedChange {
    fun TextInputEditText.isClickedChange(layout: TextInputLayout) {
        this.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && this.text.toString().isEmpty()) {
                layout.helperText = null
                layout.error = "The field can not be empty"
            }
        }
    }
}