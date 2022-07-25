/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel
            by lazy { ViewModelProvider(this).get(GameViewModel::class.java) }

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater, R.layout.game_fragment,
            container, false
        )
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this@GameFragment

        Log.i("GameFragment", "Called ViewModelProvider")
        //https://developer.android.com/reference/androidx/lifecycle/ViewModelProviders
        //viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        //setOnClickListeners()
        setObservers()
        return binding.root

    }

    private fun setOnClickListeners() {
        binding.apply {
            btnCorrect.setOnClickListener {
                viewModel.onCorrect()
            }
            btnSkip.setOnClickListener {
                viewModel.onSkip()
            }
        }
    }


    private fun setObservers() {
        viewModel.apply {
//            score.observe(this@GameFragment, Observer { newScore ->
//                updateScoreText(newScore)
//            }
//            )
//            word.observe(this@GameFragment, Observer { newWord ->
//                updateScoreWord(newWord)
//            })
//            currentTick.observe(this@GameFragment, Observer { newTimeTick ->
//                updateTimerText(newTimeTick)
//            })
            eventGameFinish.observe(this@GameFragment, Observer { hasFinished ->
                if (hasFinished) {
                    gameFinished()
                    onGameFinishComplete()
                }
            })
        }
    }

    /** Method for updating the UI **/
    private fun updateScoreText(score: Int) {
        binding.txtvScore.text = score.toString()
    }

    private fun updateScoreWord(word: String) {
        binding.txtvWord.text = word
    }

    private fun updateTimerText(time: Long) {
        binding.txtvTimer.text = DateUtils.formatElapsedTime(time)
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val currentScore = viewModel.score.value ?: 0
        val action = GameFragmentDirections.actionGameToScore(currentScore)
        findNavController(this).navigate(action)
    }
}
