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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

class SleepQualityViewModel(
    private val sleepNightKey: Long = 0L,
    val database: SleepDatabaseDao
) : ViewModel() {

    val viewModelJob = Job()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigation() {
        _navigateToSleepTracker.value = null
    }

    fun onSetSleepQualityClick(quality: Int) {
        Log.i("SleepQualityViewModel","OnClick by ${quality} ")
        uiScope.launch {
            Log.i("SleepQualityViewModel","uiScope was launch")
            updateQuality(quality)
            _navigateToSleepTracker.value = true
        }
    }

    private suspend fun updateQuality(quality: Int) {
        Log.i("SleepQualityViewModel","updateQuality method was called")
        withContext(Dispatchers.IO) {
            Log.i("SleepQualityViewModel","Into IO with nightKey ${sleepNightKey}")
            Log.i("SleepQualityViewModel","getNightByID ${database.getNightByKey(sleepNightKey)?.nightID} ")
            val night = database.getNightByKey(sleepNightKey) ?: return@withContext
            night.sleepQuality = quality
            Log.i("SleepQualityViewModel","Night is ${night.nightID} ")
            database.updateNight(night)
            Log.i("SleepQualityViewModel","Night was updated")
        }
    }
}
