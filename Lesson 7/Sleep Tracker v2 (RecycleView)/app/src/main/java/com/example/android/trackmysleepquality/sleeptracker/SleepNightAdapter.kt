package com.example.android.trackmysleepquality.sleeptracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.*
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

    var data = listOf<SleepNight>()
        set(value) {
            //sledgehummer Не делай так
            Log.i("SleepNightAdapter", "SleepNightAdapter update data")
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtvQuality: TextView = itemView.findViewById(R.id.txtvQualityString)
        val txtvLenght: TextView = itemView.findViewById(R.id.txtvSleepLenght)
        val imgvQuality: ImageView = itemView.findViewById(R.id.imgvQuality)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(item: SleepNight) {
            val res = itemView.context.resources
            item.apply {
                txtvLenght.text =
                    convertDurationToFormatted(startTimeMilli, endTimeMilli, res)
                txtvQuality.text = convertNumericQualityToString(sleepQuality, res)
                imgvQuality.setImageResource(getImgResByQuality(sleepQuality))
            }
        }
    }


}