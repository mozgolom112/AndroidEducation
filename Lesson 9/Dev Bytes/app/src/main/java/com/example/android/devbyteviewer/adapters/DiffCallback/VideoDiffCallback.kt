package com.example.android.devbyteviewer.adapters.DiffCallback

import androidx.recyclerview.widget.DiffUtil
import com.example.android.devbyteviewer.domain.Video

object VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem.url == newItem.url

}