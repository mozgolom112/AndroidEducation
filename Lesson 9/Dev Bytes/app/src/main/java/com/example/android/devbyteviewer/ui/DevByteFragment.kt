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

package com.example.android.devbyteviewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.adapters.DevByteAdapter
import com.example.android.devbyteviewer.databinding.FragmentDevByteBinding
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.util.launchUri
import com.example.android.devbyteviewer.viewmodels.DevByteViewModel

class DevByteFragment : Fragment() {

    private val viewModel: DevByteViewModel by lazy {
        initDevByteViewModel()
    }

    private fun initDevByteViewModel(): DevByteViewModel {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = DevByteViewModel.Factory(application)
        val viewModel: DevByteViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    /**
     * RecyclerView Adapter for converting a list of Video to cards.
     */
    private val devByteAdapter: DevByteAdapter by lazy {
        DevByteAdapter { makeYoutubeIntent(it) }
    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */
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
    ): FragmentDevByteBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_dev_byte, container, false)


    private fun fulfillBinding(binding: FragmentDevByteBinding) {
        binding.apply {
            viewModel = this@DevByteFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclervDevByte.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = devByteAdapter
            }
        }
    }

    private fun makeYoutubeIntent(video: Video) {
        Log.i("DevByteFragment", "${video.url}")
        val packageManager = context?.packageManager ?: return
        // Try to generate a direct intent to the YouTube app
        var intent = Intent(Intent.ACTION_VIEW, video.launchUri)
        if (intent.resolveActivity(packageManager) == null) {
            // YouTube app isn't found, use the web url
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
        }
        startActivity(intent)
    }


    private fun setObservers() {
        viewModel.apply {
            playlist.observe(viewLifecycleOwner) { videos ->
                videos?.let{
                    devByteAdapter.submitList(it)
                }
            }
        }
    }
}

