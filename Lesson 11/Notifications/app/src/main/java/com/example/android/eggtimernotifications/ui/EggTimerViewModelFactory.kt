package com.example.android.eggtimernotifications.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class EggTimerViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EggTimerViewModel::class.java)) {
            return EggTimerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
