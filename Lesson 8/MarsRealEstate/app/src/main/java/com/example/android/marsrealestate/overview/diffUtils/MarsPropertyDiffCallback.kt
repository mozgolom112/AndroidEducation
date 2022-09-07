package com.example.android.marsrealestate.overview.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.android.marsrealestate.network.MarsProperty

object DiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
    override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean =
        oldItem.id == newItem.id

}