package com.example.android.devbyteviewer.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.databinding.DevbyteItemBinding
import com.example.android.devbyteviewer.domain.Video


class DevByteViewHolder private constructor(private val binding: DevbyteItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item

        fun from(parent: ViewGroup): DevByteViewHolder = DevByteViewHolder(getBinding(parent))

        private fun getBinding(parent: ViewGroup): DevbyteItemBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            return DevbyteItemBinding.inflate(layoutInflater, parent, false)
        }
    }

    fun bind(inputVideo: Video, clickListener: (Video) -> Unit) {
        binding.apply {
            itemView.setOnClickListener() { clickListener(inputVideo) }
            video = inputVideo
        }
    }
}

//Without binding
//class DevByteViewHolder(private val view: View) :
//    RecyclerView.ViewHolder(view) {
//    private val titleText: TextView = view.findViewById(R.id.txtvTitle)
//    private val shortDescriptionText: TextView = view.findViewById(R.id.txtvDescription)
//    private val videoThumbnail: ImageView = view.findViewById(R.id.imgvVideoThumbnail)
//
//    companion object {
//        @LayoutRes
//        val LAYOUT = R.layout.devbyte_item
//    }
//
//    fun bind(inputVideo: Video, clickListener: (Video) -> Unit) = inputVideo.let {
//        itemView.setOnClickListener { clickListener(inputVideo) }
//        titleText.text = it.title
//        shortDescriptionText.text = it.shortDescription()
//        videoThumbnail.setImageUrl(it.thumbnail)
//    }
//
//}