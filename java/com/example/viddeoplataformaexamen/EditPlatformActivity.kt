package com.example.viddeoplataformaexamen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditPlatformActivity : AppCompatActivity() {

    private lateinit var dbHelper: VideoDatabaseHelper
    private var platformId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_platform)

        val nameEditText: EditText = findViewById(R.id.editPlatformName)
        val priceEditText: EditText = findViewById(R.id.editPlatformPrice)
        val videoQualityEditText: EditText = findViewById(R.id.editPlatformVideoQuality)
        val relatedPlatformsEditText: EditText = findViewById(R.id.editPlatformRelatedPlatforms)
        val audienceEditText: EditText = findViewById(R.id.editPlatformAudience)
        val saveButton: Button = findViewById(R.id.saveButton)
        val addVideoButton: Button = findViewById(R.id.addVideoButton)
        val viewVideosButton: Button = findViewById(R.id.viewVideosButton)
        val deleteButton: Button = findViewById(R.id.deletePlatformButton)

        dbHelper = VideoDatabaseHelper(this)

        platformId = intent.getLongExtra("PLATFORM_ID", 0)
        val platform = dbHelper.getPlatformById(platformId)

        platform?.let {
            nameEditText.setText(it["name"] as String)
            priceEditText.setText(it["price"].toString())
            videoQualityEditText.setText(it["videoQuality"] as String)
            relatedPlatformsEditText.setText(it["relatedPlatforms"] as String)
            audienceEditText.setText(it["audience"].toString())
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val videoQuality = videoQualityEditText.text.toString()
            val relatedPlatforms = relatedPlatformsEditText.text.toString()
            val audience = audienceEditText.text.toString().toIntOrNull() ?: 0

            if (dbHelper.updatePlatform(platformId, name, price, videoQuality, relatedPlatforms, audience)) {
                Toast.makeText(this, "Platform updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity and return to the previous one
            } else {
                Toast.makeText(this, "Error updating platform", Toast.LENGTH_SHORT).show()
            }
        }

        addVideoButton.setOnClickListener {
            val intent = Intent(this, AddVideoActivity::class.java)
            intent.putExtra("PLATFORM_ID", platformId)
            startActivity(intent)
        }

        viewVideosButton.setOnClickListener {
            val intent = Intent(this, VideoListActivity::class.java)
            intent.putExtra("PLATFORM_ID", platformId)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            val success = deletePlatformWithVideos(platformId)
            if (success) {
                Toast.makeText(this, "Platform deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity and return to the previous one
            } else {
                Toast.makeText(this, "Failed to delete platform", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deletePlatformWithVideos(platformId: Long): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        return try {
            // Delete all videos associated with the platform
            db.delete(
                VideoDatabaseHelper.TABLE_VIDEOS,
                "${VideoDatabaseHelper.COLUMN_PLATFORM_ID_FK} = ?",
                arrayOf(platformId.toString())
            )

            // Delete the platform
            val rowsAffected = db.delete(
                VideoDatabaseHelper.TABLE_PLATFORMS,
                "${VideoDatabaseHelper.COLUMN_PLATFORM_ID} = ?",
                arrayOf(platformId.toString())
            )

            db.setTransactionSuccessful()
            rowsAffected > 0
        } finally {
            db.endTransaction()
        }
    }
}
