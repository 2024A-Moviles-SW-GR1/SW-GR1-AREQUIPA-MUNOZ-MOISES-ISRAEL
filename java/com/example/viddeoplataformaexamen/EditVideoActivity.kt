// EditVideoActivity.kt
package com.example.viddeoplataformaexamen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditVideoActivity : AppCompatActivity() {

    private lateinit var dbHelper: VideoDatabaseHelper
    private var videoId: Long? = null
    private var platformId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_video)

        dbHelper = VideoDatabaseHelper(this)

        platformId = intent.getLongExtra("PLATFORM_ID", 0)
        videoId = intent.getLongExtra("VIDEO_ID", -1)

        val nameEditText: EditText = findViewById(R.id.videoNameEditText)
        val durationEditText: EditText = findViewById(R.id.videoDurationEditText)
        val publicationDateEditText: EditText = findViewById(R.id.videoPublicationDateEditText)
        val qualityEditText: EditText = findViewById(R.id.videoQualityEditText)
        val languageEditText: EditText = findViewById(R.id.videoLanguageEditText)
        val saveButton: Button = findViewById(R.id.saveButton)
        val deleteButton: Button = findViewById(R.id.deleteButton)

        if (videoId != -1L) {
            // Load existing video data
            val video = dbHelper.getVideoById(videoId!!)
            nameEditText.setText(video["name"] as String)
            durationEditText.setText(video["duration"] as String)
            publicationDateEditText.setText(video["publicationDate"] as String)
            qualityEditText.setText(video["quality"] as String)
            languageEditText.setText(video["language"] as String)
            deleteButton.visibility = Button.VISIBLE
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val duration = durationEditText.text.toString()
            val publicationDate = publicationDateEditText.text.toString()
            val quality = qualityEditText.text.toString()
            val language = languageEditText.text.toString()

            if (videoId == -1L) {
                // Add new video
                dbHelper.addVideoToPlatform(name, duration, publicationDate, quality, language, platformId)
            } else {
                // Update existing video
                dbHelper.updateVideo(videoId!!, name, duration, publicationDate, quality, language)
            }
            finish()
        }

        deleteButton.setOnClickListener {
            videoId?.let {
                dbHelper.deleteVideo(it)
                finish()
            }
        }
    }
}
