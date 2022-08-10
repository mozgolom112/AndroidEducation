package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {
    private val _score by lazy { MutableLiveData<Int>() }
    val score: LiveData<Int>
        get() = _score

    private val _eventPlayAgain by lazy { MutableLiveData<Boolean>() }
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        Log.i("ScoreViewModel", "Final score is $finalScore")
        _score.value = finalScore
        _eventPlayAgain.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ScoreViewModel", "Destroyed")
    }

    fun onPlayAgainButton() {
        _eventPlayAgain.value = true
    }
}