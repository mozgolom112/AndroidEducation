/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    private val sleepQualityViewModel: SleepQualityViewModel by lazy {
        initSleepQualityViewModel()
    }

    private fun initSleepQualityViewModel(): SleepQualityViewModel {
        val application = requireNotNull(this.activity).application
        val sleepNightKey = navArgs<SleepQualityFragmentArgs>().value.sleepNightKey
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(sleepNightKey, dataSource)
        val viewModel: SleepQualityViewModel by viewModels { viewModelFactory }

        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSleepQualityBinding =
        DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_quality, container, false
        )

    private fun fulfillBinding(binding: FragmentSleepQualityBinding) {
        binding.apply {
            sleepQualityViewModel = this@SleepQualityFragment.sleepQualityViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun setObservers() {
        sleepQualityViewModel.apply {
            navigateToSleepTracker.observe(viewLifecycleOwner, Observer { hasNavigate ->

                if (hasNavigate == true) {
                    navigateToSleepTracker()
                }
            })
        }
    }

    private fun navigateToSleepTracker() {
        val action =
            SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
        findNavController().navigate(action)
        sleepQualityViewModel.doneNavigation()
    }
}
