// VideoManagementActivity.kt
package com.example.viddeoplataformaexamen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class VideoManagementActivity : AppCompatActivity() {

    private lateinit var dbHelper: VideoDatabaseHelper
    private var platformId: Long = 0
    private lateinit var videoListView: ListView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var videos: List<Map<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_management)

        platformId = intent.getLongExtra("PLATFORM_ID", 0)
        dbHelper = VideoDatabaseHelper(this)

        videoListView = findViewById(R.id.videoListView)
        val addVideoButton: Button = findViewById(R.id.addVideoButton)

        loadVideos()
        videoAdapter = VideoAdapter(this, videos)
        videoListView.adapter = videoAdapter

        addVideoButton.setOnClickListener {
            val intent = Intent(this, EditVideoActivity::class.java)
            intent.putExtra("PLATFORM_ID", platformId)
            startActivity(intent)
        }

        videoListView.setOnItemClickListener { _, _, position, _ ->
            val video = videos[position]
            val intent = Intent(this, EditVideoActivity::class.java)
            intent.putExtra("VIDEO_ID", video["id"] as Long)
            startActivity(intent)
        }
    }

    private fun loadVideos() {
        videos = dbHelper.getVideosForPlatform(platformId)
        videoAdapter.updateVideos(videos)
    }
}
