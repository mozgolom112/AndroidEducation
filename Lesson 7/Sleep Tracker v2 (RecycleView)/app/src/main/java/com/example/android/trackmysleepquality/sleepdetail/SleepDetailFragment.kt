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

package com.example.android.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepDetailBinding
import com.example.android.trackmysleepquality.sleepquality.SleepQualityFragmentArgs
import com.example.android.trackmysleepquality.sleepquality.SleepQualityFragmentDirections


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SleepDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SleepDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SleepDetailFragment : Fragment() {

    private val sleepDetailViewModel: SleepDetailViewModel by lazy{
        initSleepDetailViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        return binding.root
    }
    private fun initSleepDetailViewModel(): SleepDetailViewModel {
        val application = requireNotNull(this.activity).application
        val sleepNightKey = navArgs<SleepQualityFragmentArgs>().value.sleepNightKey
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(sleepNightKey, dataSource)
        val viewModel:SleepDetailViewModel  by viewModels { viewModelFactory }
        return viewModel
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSleepDetailBinding =
        DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_detail, container, false
        )

    private fun fulfillBinding(binding: FragmentSleepDetailBinding) {
        binding.apply {
            sleepDetailViewModel = this@SleepDetailFragment.sleepDetailViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }

    private fun setObservers() {
        sleepDetailViewModel.apply {
            navigateToSleepTracker.observe(viewLifecycleOwner, Observer { hasNavigate ->
                if (hasNavigate == true) {
                    navigateToSleepTracker()
                    sleepDetailViewModel.doneNavigating()
                }
            })
        }
    }

    private fun navigateToSleepTracker(){
        val action = SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
        findNavController().navigate(action)
    }



}
