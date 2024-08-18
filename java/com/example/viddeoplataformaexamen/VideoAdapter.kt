// VideoAdapter.kt
package com.example.viddeoplataformaexamen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class VideoAdapter(private val context: Context, private var videos: List<Map<String, Any>>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return videos.size
    }

    override fun getItem(position: Int): Any {
        return videos[position]
    }

    override fun getItemId(position: Int): Long {
        return (getItem(position) as Map<String, Any>)["id"] as Long
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_video, parent, false)

        val video = getItem(position) as Map<String, Any>
        val nameTextView: TextView = view.findViewById(R.id.videoNameTextView)
        val durationTextView: TextView = view.findViewById(R.id.videoDurationTextView)
        val qualityTextView: TextView = view.findViewById(R.id.videoQualityTextView)

        nameTextView.text = video["name"] as String
        durationTextView.text = "Duration: ${video["duration"]}"
        qualityTextView.text = "Quality: ${video["quality"]}"

        return view
    }

    fun updateVideos(newVideos: List<Map<String, Any>>) {
        videos = newVideos
        notifyDataSetChanged()
    }
}
