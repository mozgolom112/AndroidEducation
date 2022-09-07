package com.example.android.marsrealestate.overview.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class MarsPropertyViewHolder private constructor(private val binding: GridViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): MarsPropertyViewHolder =
            MarsPropertyViewHolder(getBinding(parent))

        private fun getBinding(parent: ViewGroup): GridViewItemBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = GridViewItemBinding.inflate(layoutInflater, parent, false)
            return binding
        }
    }

    fun bind(item: MarsProperty) =
        fulfillBinding(item)

    private fun fulfillBinding(item: MarsProperty,) {
        binding.apply {
            property = item
            executePendingBindings()
        }
    }
}