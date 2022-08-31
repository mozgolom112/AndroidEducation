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

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tonight = MutableLiveData<SleepNight?>()
    val tonight: LiveData<SleepNight?>
        get() = _tonight

    val nights = database.getAllNights()
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    fun doneNavigation() {
        _navigateToSleepQuality.value = null
    }

    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }

    val stopButtonVisible = Transformations.map(tonight) {
        it != null
    }

    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneSnackBarEvent() {
        _showSnackbarEvent.value = false
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            _tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.startTimeMilli != night?.endTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTrackingClick() {
        uiScope.launch {
            val newNight = SleepNight()
            Log.i("onStartTrackingClick", "Stage0: Before insert")
            insert(newNight)
            Log.i("onStartTrackingClick", "Stage3: After insert")
            _tonight.value = getTonightFromDatabase()
            Log.i("onStartTrackingClick", "Stage4: Finish update value")

        }
    }

    private val _navigateToSleepNightDetail = MutableLiveData<Long?>()
    val navigateToSleepNightDetail: LiveData<Long?>
        get() = _navigateToSleepNightDetail

    fun onSleepNightClicked(nightID: Long) {
        _navigateToSleepNightDetail.value = nightID
    }

    fun onSleepDataDetailNavigated() {
        _navigateToSleepNightDetail.value = null
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            Log.i("onStartTrackingClick", "Stage1.1: Start insert")
            database.insertNight(night)
            Log.i("onStartTrackingClick", "Stage1.2: Finish insert")
        }
    }

    fun onStopTrackingClick() {
        uiScope.launch {
            //0) Посмотреть предыдущую ночь
            val oldNight = _tonight.value ?: return@launch
            //1) Записать время
            oldNight.endTimeMilli = System.currentTimeMillis()
            //2) Вызвать suspend функцию для update записи
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.updateNight(night)
        }
    }

    fun onClearClick() {
        uiScope.launch {
            clear()
            _tonight.value = null
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clearNights()
        }
    }

    fun onSleepNightDeleteClicked(nightID: Long) {
        uiScope.launch {
            deleteSleepNightByKey(nightID)
            if (nightID == _tonight.value?.nightID) {
                _tonight.value = null
            }
        }
    }

    private suspend fun deleteSleepNightByKey(nightID: Long){
        withContext(Dispatchers.IO){
            database.deleteNightByKey(nightID)
        }
    }

}

