// VideoListActivity.kt
package com.example.viddeoplataformaexamen

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class VideoListActivity : AppCompatActivity() {

    private lateinit var dbHelper: VideoDatabaseHelper
    private var platformId: Long = 0
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        dbHelper = VideoDatabaseHelper(this)

        platformId = intent.getLongExtra("PLATFORM_ID", 0)
        val videoListView: ListView = findViewById(R.id.videoListView)

        val videos = dbHelper.getVideosForPlatform(platformId)
        videoAdapter = VideoAdapter(this, videos)
        videoListView.adapter = videoAdapter
    }

    override fun onResume() {
        super.onResume()
        val videos = dbHelper.getVideosForPlatform(platformId)
        videoAdapter.updateVideos(videos)
    }
}
