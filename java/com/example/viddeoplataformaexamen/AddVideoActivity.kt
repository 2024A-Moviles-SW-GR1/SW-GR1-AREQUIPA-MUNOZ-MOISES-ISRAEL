package com.example.viddeoplataformaexamen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddVideoActivity : AppCompatActivity() {

    private lateinit var dbHelper: VideoDatabaseHelper
    private var platformId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)

        dbHelper = VideoDatabaseHelper(this)
        platformId = intent.getLongExtra("PLATFORM_ID", -1)

        if (platformId == -1L) {
            Toast.makeText(this, "Invalid Platform ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val titleEditText: EditText = findViewById(R.id.videoTitleEditText)
        val durationEditText: EditText = findViewById(R.id.videoDurationEditText)
        val qualityEditText: EditText = findViewById(R.id.videoQualityEditText)
        val languageEditText: EditText = findViewById(R.id.videoLanguageEditText)
        val publicationDateEditText: EditText = findViewById(R.id.videoPublicationDateEditText)
        val saveButton: Button = findViewById(R.id.saveVideoButton)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val duration = durationEditText.text.toString()
            val quality = qualityEditText.text.toString()
            val language = languageEditText.text.toString()
            val publicationDate = publicationDateEditText.text.toString()

            if (title.isNotBlank() && duration.isNotBlank() && quality.isNotBlank() &&
                language.isNotBlank() && publicationDate.isNotBlank()) {

                val result = dbHelper.addVideoToPlatform(title, duration, publicationDate, quality, language, platformId)

                if (result > 0) {
                    Toast.makeText(this, "Video added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad y vuelve a la anterior
                } else {
                    Toast.makeText(this, "Failed to add video", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
