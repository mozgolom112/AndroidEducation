package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1
//TODO("Try to use enum class instead of hardcode values")
//enum class ITEM_VIEW_TYPE{
//    HEADER, ITEM
//}
//TODO(STEP 2. Change in ListAdapter on unified DataItem and provide to accept all ViewHolders)
class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
    //TODO(STEP 3. Override this method to provided binding separately )
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                //TODO(STEP 4. Don't forget that now you have different types of DataItem)
                val night = getItem(position) as DataItem.SleepNightItem
                holder.bind(night.sleepNight, clickListener)
            }
            //is OtherViewHolder -> {...}
        }
    }
    //TODO(STEP 5. Change return type on common RecyclerView.ViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(parent, viewType)
    //TODO(STEP 6. Create unique ViewHolder)
    private fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }
    //TODO(STEP 7. Now you couldn't sumbit list, because you have to combine all DataItem in one list
    //TODO It is better to do in background, so use coroutines here inspired by Dispatchers.default)
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list){
                null -> listOf<DataItem>(DataItem.Header)
                else -> listOf<DataItem>(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            //TODO(STEP 8. Dont forget to submit new list of items)
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

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

        fun bind(item: SleepNight, clickListener: SleepNightListener) =
            fulfillBinding(item, clickListener)

        private fun fulfillBinding(night: SleepNight, clickListener: SleepNightListener) {
            binding.apply {
                sleepNight = night
                this.clickListener = clickListener
                executePendingBindings()
            }
        }
    }

    class TextViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    //TODO(STEP 8. Don't forget to change DiffUtils. Logic might be different. In this way for headers
    //TODO we use the Long.MIN_VALUE, otherwise we use sleepNight.nightID)
    class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem.id == newItem.id

        //не факт, что сравнение будет корректным.
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem == newItem
    }

    class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
        fun onClick(night: SleepNight) = clickListener(night.nightID)
    }

}

//TODO(STEP 1. Create a seal class with DataItem)
sealed class DataItem() {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightID
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}