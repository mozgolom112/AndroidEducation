package com.example.android.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.getImgResByQuality

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?){
    item?.let {
        text = convertDurationToFormatted(it.startTimeMilli, it.endTimeMilli, context.resources)
    }
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?){
    item?.let {
        text = convertNumericQualityToString(it.sleepQuality, context.resources)
    }
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?){
    item?.let {
        setImageResource(getImgResByQuality(it.sleepQuality))
    }
}