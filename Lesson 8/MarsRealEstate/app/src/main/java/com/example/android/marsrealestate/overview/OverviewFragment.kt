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
 *
 */

package com.example.android.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.overview.adapters.PhotoGridAdapter
import com.google.android.material.snackbar.Snackbar

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val overviewViewModel: OverviewViewModel by lazy {
        initOverviewViewModel()
    }

    private val photoGridAdapter: PhotoGridAdapter by lazy {
        val clickListener = PhotoGridAdapter.OnItemClickListener {
            overviewViewModel.displayPropertyDetails(it)
        }
        PhotoGridAdapter(clickListener)
    }

    private fun initOverviewViewModel(): OverviewViewModel {
        val viewModelFactory = OverviewModelFactory()
        val viewModel: OverviewViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = initBinding(inflater, container)
        fulfillBinding(binding)
        setObservers()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOverviewBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_overview, container, false)

    private fun fulfillBinding(binding: FragmentOverviewBinding) {
        binding.apply {
            // Giving the binding access to the OverviewViewModel
            viewModel = this@OverviewFragment.overviewViewModel
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            recycleviewPhotosGrid.adapter = photoGridAdapter
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val filter = when (item.itemId) {
            R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
            R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
            else -> MarsApiFilter.SHOW_ALL
        }
        overviewViewModel.updateFilter(filter)
        return true
    }

    private fun setObservers() {
        overviewViewModel.apply {
            navigateToSelectedProperty.observe(viewLifecycleOwner, Observer { property ->
                navigateToPropertyDetails(property)
            })
        }
    }

    private fun showSnackBar(snackBarText: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            snackBarText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateToPropertyDetails(property: MarsProperty?) {
        property?.let {
            val action = OverviewFragmentDirections.actionShowDetail(property)
            findNavController().navigate(action)
            overviewViewModel.displayPropertyDetailsComplete()
        }

    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
