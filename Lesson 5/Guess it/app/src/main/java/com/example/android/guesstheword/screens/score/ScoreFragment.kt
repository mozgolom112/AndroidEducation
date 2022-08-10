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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    private val viewModelFactory: ScoreViewModelFactory
            by lazy {
                Log.i("ScoreFragment", "viewModelFactory by lazy called")
                ScoreViewModelFactory(navArgs<ScoreFragmentArgs>().value.score)
            }
    private val viewModel: ScoreViewModel
            by lazy {
                Log.i("ScoreFragment", "viewModel by lazy called")
                ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)
            }
    private lateinit var binding: ScoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("ScoreFragment", "onCreateView called")
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.score_fragment, container, false
        )
        binding.apply {
            scoreViewModel = viewModel
            lifecycleOwner = this@ScoreFragment
        }

        // Get args using by navArgs property delegate
        //setClickListeners()
        setObservers()

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            btnPlayAgain.setOnClickListener { viewModel.onPlayAgainButton() }
        }
    }

    private fun setObservers() {
        viewModel.apply {
//            score.observe(this@ScoreFragment, Observer { newScore ->
//                Log.i("ScoreFragment", "Observe was called")
//                updateScoreText(newScore)
//            })
            eventPlayAgain.observe(this@ScoreFragment, Observer { hasPlayAgain ->
                if (hasPlayAgain) {
                    onPlayAgain()
                }
            })
        }
    }

    private fun updateScoreText(score: Int) {
        binding.txtvScore.text = score.toString()
    }

    private fun onPlayAgain() {
        findNavController().navigate(ScoreFragmentDirections.actionRestart())
    }
}
