package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L
        private const val ONE_TICK_INTERVAL = 1000L //mills
        private const val COUNTDOWN_TIME = 10000L  //mils
    }

    // The current word
    private val _word by lazy { MutableLiveData<String>() }
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score by lazy { MutableLiveData<Int>() }
    val score: LiveData<Int>
        get() = _score
    private val _eventGameFinish by lazy { MutableLiveData<Boolean>() }

    // Current state of timer
    private val _currentTick by lazy { MutableLiveData<Long>() }
    val currentTick: LiveData<Long>
        get() = _currentTick

    val currentTickString = Transformations.map(currentTick) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // Finish game event
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val timer by lazy {
        object : CountDownTimer(COUNTDOWN_TIME, ONE_TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTick.value = millisUntilFinished / ONE_TICK_INTERVAL
            }

            override fun onFinish() {
                _eventGameFinish.value = true
            }
        }
    }

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        _eventGameFinish.value = false
        _currentTick.value = DONE
        _score.value = 0
        _word.value = ""
        resetList()
        nextWord()

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = getWords()
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //_eventGameFinish.value = true
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    private fun getWords(): MutableList<String> {
        return mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
    }
}