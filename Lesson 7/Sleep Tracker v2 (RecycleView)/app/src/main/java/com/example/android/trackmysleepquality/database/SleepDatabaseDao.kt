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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao{
    //Вставить новую запись о ночи
    @Insert
    fun insertNight(night: SleepNight)

    //Обновить запись о ночи
    @Update
    fun updateNight(night: SleepNight)

    //TODO("Понять почему нельзя из LiveData вытащить поля в SleepQualityViewModel -> updateQuality -> withContext -> db.get")
    @Query("SELECT * FROM daily_sleep_qualities where night_id = :key")
    fun getNightByKeyLiveData(key: Long): LiveData<SleepNight>

    //Извлечь запись о ночи из БД по ключу
    @Query("SELECT * FROM daily_sleep_qualities WHERE night_id = :key")
    fun getNightByKey(key: Long): SleepNight?

    //Извлечь записи о ночи из БД по списку ключей
    @Query("SELECT * FROM daily_sleep_qualities WHERE night_id IN (:keyList)")
    fun getNightByKeyList(keyList: List<Long>): List<SleepNight>

    //Удалить записи о ночи из списка
    @Delete
    fun deleteNights(nights: List<SleepNight>)

    //Удалить все записи
    @Query("DELETE FROM daily_sleep_qualities")
    fun clearNights()

    //Извлечь все записи о ночи из БД
    @Query("SELECT * FROM daily_sleep_qualities ORDER BY night_id DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    //Извлечь последнюю запись о ночи
    @Query("SELECT * FROM daily_sleep_qualities ORDER BY night_id DESC LIMIT 1")
    fun getTonight(): SleepNight?
}


