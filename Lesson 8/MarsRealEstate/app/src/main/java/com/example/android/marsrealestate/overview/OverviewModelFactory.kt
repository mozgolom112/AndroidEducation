package com.example.android.marsrealestate.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OverviewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}