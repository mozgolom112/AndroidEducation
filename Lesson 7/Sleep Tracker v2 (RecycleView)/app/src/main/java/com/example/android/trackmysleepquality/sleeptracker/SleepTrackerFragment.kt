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

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    private val sleepTrackerViewModel: SleepTrackerViewModel by lazy {
        initSleepTrackerViewModel()
    }

    private val sleepNightAdapter: SleepNightAdapter by lazy {
        Log.i("SleepNightAdapter", "SleepNightAdapter was created")
        val clickListener = { nightID: Long ->
            //sleepTrackerViewModel.onSleepNightClicked(nightID)
            //TODO(Спросить пользователя, уверен ли что он хочет удалить)
            //TODO(https://material.io/components/dialogs/android#alert-dialog)
//            var isDelete = false
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle("Удаление записи")
//                .setMessage("Вы уверены что хотите удалить эту запись (ID: ${nightID})? ")
//                .setNeutralButton("Вернуться назад") { dialog, which ->
//                    isDelete = false
//                }
//                .setNegativeButton("Отмена") { dialog, which ->
//                    isDelete = false
//                }
//                .setPositiveButton("Удалить") { dialog, which ->
//                    isDelete = true
//                }
//                .show()
//            if (isDelete) {
//                sleepTrackerViewModel.onSleepNightDeleteClicked(nightID)
//            }
            sleepTrackerViewModel.onSleepNightDeleteClicked(nightID)
        }
        SleepNightAdapter(clickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSleepTrackerBinding = DataBindingUtil.inflate(
        inflater, R.layout.fragment_sleep_tracker, container, false
    )

    private fun fulfillBinding(binding: FragmentSleepTrackerBinding) {
        binding.apply {
            sleepTrackerViewModel = this@SleepTrackerFragment.sleepTrackerViewModel
            lifecycleOwner = viewLifecycleOwner
            recyclevSleepList.apply {
                Log.i("SleepNightAdapter", "attach SleepNightAdapter to RV")
                adapter = sleepNightAdapter
                layoutManager = customManager()
            }
        }
    }

    private fun customManager(): GridLayoutManager {
        val manager = GridLayoutManager(activity, 4)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 4
                else -> 1
            }
        }
        return manager
    }

    private fun setObservers() {
        sleepTrackerViewModel.apply {
            navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
                navigateToSleepQuality(night)
            })

            navigateToSleepNightDetail.observe(viewLifecycleOwner, Observer { nightID ->
                navigateToSleepNightDetail(nightID)
            })
            showSnackBarEvent.observe(viewLifecycleOwner, Observer { hasShowen ->
                if (hasShowen == true) {
                    showSnackBar()
                }
            })
            nights.observe(viewLifecycleOwner, Observer { nights ->
                nights?.let {
                    sleepNightAdapter.addHeaderAndSubmitList(it)
                }
            })
        }

    }

    private fun navigateToSleepNightDetail(nightID: Long?) {
        nightID?.let {
            val action =
                SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepDetailFragment(it)
            findNavController().navigate(action)
            sleepTrackerViewModel.onSleepDataDetailNavigated()
        }

    }

    private fun navigateToSleepQuality(night: SleepNight?) {
        night?.let {
            val action =
                SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(
                    night.nightID
                )
            findNavController().navigate(action)
            sleepTrackerViewModel.doneNavigation()
        }
    }


    private fun showSnackBar() {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(R.string.cleared_message),
            Snackbar.LENGTH_SHORT
        ).show()
        sleepTrackerViewModel.doneSnackBarEvent()
    }

    private fun initSleepTrackerViewModel(): SleepTrackerViewModel {
        val application = requireNotNull(this.activity).application
        val dataSource: SleepDatabaseDao = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory =
            SleepTrackerViewModelFactory(dataSource, application)
        //Простой вариант
        //val sleepTrackerViewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)
        //Классный вариант
        val sleepTrackerViewModel: SleepTrackerViewModel by viewModels { viewModelFactory }
        return sleepTrackerViewModel
    }
}
