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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch

enum class MarsApiStatus {LOADING, DONE, ERROR}
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) =
        viewModelScope.launch {
            try {
                /**
                 * The Retrofit service returns a coroutine Deferred, which we await to get
                 * the result of the transaction.
                 */
                _status.value = MarsApiStatus.LOADING
                var listResult = MarsApi.retrofitService.getPropertiesAsync(filter.value)
                _status.value = MarsApiStatus.DONE
                if (listResult.isNotEmpty()) {
                    _properties.value = listResult
                } else {
                    _properties.value = ArrayList()
                }

            } catch (e: Throwable) {
                _status.value = MarsApiStatus.ERROR

            }
        }

    private val _navigateToSelectedProperty  = MutableLiveData<MarsProperty?>()
    val navigateToSelectedProperty: LiveData<MarsProperty?>
        get() = _navigateToSelectedProperty

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete(){
        _navigateToSelectedProperty.value = null
    }

    fun updateFilter(filter: MarsApiFilter) = getMarsRealEstateProperties(filter)
}
