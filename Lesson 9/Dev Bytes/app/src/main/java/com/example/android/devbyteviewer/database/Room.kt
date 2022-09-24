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

package com.example.android.devbyteviewer.database

import android.app.appsearch.SetSchemaResponse
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration


@Dao
interface VideoDao {
    @Query("select * from videos")
    fun getVideos(): LiveData<List<DatabaseVideo>> //Не забывай оборачивать в LiveData (или позже используй Flow)
    //Это необхоимо чтобы это не висело на UI потоке и автоматически закидывалось в background
    //Также ее легче обзервить observe

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}

@Database(entities = [DatabaseVideo::class], version = 1, exportSchema = false)
abstract class VideoDatabase : RoomDatabase() {
    abstract val videoDao: VideoDao

    companion object {
        //Применяем паттерн Singleton
        private lateinit var INSTANCE: VideoDatabase

        fun getInstance(context: Context): VideoDatabase {
            //Не забывай оборачивать тут в корутину, чтобы сделать создание инстанса потокобезопасным (thread safe)
            //Замком (lock) может быть что угодно
            synchronized(VideoDatabase::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        VideoDatabase::class.java, "videos"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}