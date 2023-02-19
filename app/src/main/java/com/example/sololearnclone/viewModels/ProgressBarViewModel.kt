package com.example.sololearnclone.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProgressBarViewModel : ViewModel() {
    val progress: MutableStateFlow<Int> = MutableStateFlow(15)
    val lastPosition: MutableStateFlow<Int> = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            progress.value = progress.value
            lastPosition.value = lastPosition.value
        }
    }
}