package com.example.android.devbyteviewer.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android.devbyteviewer.adapters.DiffCallback.VideoDiffCallback
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.viewholders.DevByteViewHolder

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class DevByteAdapter(private val clickListener: (Video) -> Unit) :
    ListAdapter<Video, DevByteViewHolder>(VideoDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder =
        DevByteViewHolder.from(parent)

    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}