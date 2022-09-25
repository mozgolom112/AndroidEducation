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

package com.example.android.devbyteviewer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideoDatabase
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.util.asDatabaseModel
import com.example.android.devbyteviewer.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class VideosRepository(private val database: VideoDatabase) {
    //Transformations.map is perfect for mapping the output of one LiveData to another type.
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() = withContext(Dispatchers.IO) {
        try {
            val playlist = Network.devbytes.getPlaylist().await()
            //Note the asterisk * is the spread operator.
            //It allows you to pass in an array to a function that expects varargs.
            //https://kotlinlang.org/docs/functions.html#variable-number-of-arguments-varargs
            //https://kotlinlang.org/docs/functions.html#named-arguments
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        } catch (e: Exception) {
            Log.e("DevByteViewModel", "Updated playlist not available $e")
            throw(e)
        }
    }


}