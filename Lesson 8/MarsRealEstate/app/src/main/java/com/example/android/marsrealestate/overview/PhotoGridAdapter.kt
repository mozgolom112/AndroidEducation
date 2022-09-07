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

package com.example.android.marsrealestate.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

private val ITEM_VIEW_TYPE_ITEM = 1


class PhotoGridAdapter(private val clickListener: onClickListener) :
    ListAdapter<MarsProperty, RecyclerView.ViewHolder>(MarsPropertyDiffCallback) {
    companion object MarsPropertyDiffCallback : DiffUtil.ItemCallback<MarsProperty>() {
        override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean =
            oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(parent, viewType)

    private fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_ITEM -> MarsPropertyViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MarsProperty -> ITEM_VIEW_TYPE_ITEM
            else -> throw ClassCastException("Unknown viewType ${getItem(position)}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MarsPropertyViewHolder -> {
                val marsProperty = getItem(position)
                holder.apply {
                    bind(marsProperty)
                    itemView.setOnClickListener{
                        clickListener.onClick(marsProperty)
                    }
                }
            }
        }
    }

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

    class onClickListener(val clickListener: (MarsProperty)->Unit) {
        fun onClick(marsProperty: MarsProperty) = clickListener(marsProperty)
    }
}