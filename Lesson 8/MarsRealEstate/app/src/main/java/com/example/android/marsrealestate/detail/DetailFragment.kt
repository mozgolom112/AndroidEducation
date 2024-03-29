/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentDetailBinding
import com.example.android.marsrealestate.network.MarsProperty

/**
 * This [Fragment] will show the detailed information about a selected piece of Mars real estate.
 */
class DetailFragment : Fragment() {
    private val detailViewModel by lazy {
        initDetailViewModel()
    }

    private fun initDetailViewModel(): DetailViewModel {
        val application = requireNotNull(this.activity).application
        val marsProperty = navArgs<DetailFragmentArgs>().value.marsProperty
        val viewModelFactory = DetailViewModelFactory(marsProperty, application)
        val viewModel: DetailViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = initBinding(inflater, container)
        fulfillBinding(binding)
        return binding.root
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

    private fun fulfillBinding(binding: FragmentDetailBinding) {
        binding.apply {
            viewModel = this@DetailFragment.detailViewModel
            lifecycleOwner = viewLifecycleOwner
        }
    }
}
