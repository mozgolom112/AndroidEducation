package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(parent)

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder = ViewHolder(getBinding(parent))

            private fun getBinding(parent: ViewGroup): ListItemSleepNightBinding {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return binding
            }
        }

        fun bind(item: SleepNight, clickListener: SleepNightListener) = fulfillBinding(item, clickListener)

        private fun fulfillBinding(night: SleepNight, clickListener: SleepNightListener) {
            binding.apply {
                sleepNight = night
                this.clickListener = clickListener
                executePendingBindings()
            }
        }
    }

    class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight) =
            oldItem.nightID == newItem.nightID
        //Сравнение корректно, так как за нас сделано сравнение в датаклассе
        //под коробкой он сравнивает все поля
        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight) =
            oldItem == newItem
    }

    class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
        fun onClick(night: SleepNight) = clickListener(night.nightID)
    }
}