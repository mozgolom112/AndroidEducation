package com.example.android.devbyteviewer.util

import com.example.android.devbyteviewer.database.DatabaseVideo
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.NetworkVideoContainer

fun List<DatabaseVideo>.asDomainModel(): List<Video> = map {
    Video(
        title = it.title,
        description = it.description,
        url = it.url,
        updated = it.updated,
        thumbnail = it.thumbnail
    )
}

/**
 * Convert Network results to database objects
 */
fun NetworkVideoContainer.asDomainModel(): List<Video> = videos.map {
    Video(
        title = it.title,
        description = it.description,
        url = it.url,
        updated = it.updated,
        thumbnail = it.thumbnail
    )
}


fun NetworkVideoContainer.asDatabaseModel(): Array<DatabaseVideo> = videos.map {
    DatabaseVideo(
        title = it.title,
        description = it.description,
        url = it.url,
        updated = it.updated,
        thumbnail = it.thumbnail
    )
}.toTypedArray()