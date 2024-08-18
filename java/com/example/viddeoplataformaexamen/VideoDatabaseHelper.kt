package com.example.viddeoplataformaexamen;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VideoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "videoplatform.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_PLATFORMS = "platforms"
        const val COLUMN_PLATFORM_ID = "id"
        private const val COLUMN_PLATFORM_NAME = "name"
        private const val COLUMN_PLATFORM_PRICE = "price"
        private const val COLUMN_PLATFORM_VIDEO_QUALITY = "video_quality"
        private const val COLUMN_PLATFORM_RELATED_PLATFORMS = "related_platforms"
        private const val COLUMN_PLATFORM_AUDIENCE = "audience"

        const val TABLE_VIDEOS = "videos"
        private const val COLUMN_VIDEO_ID = "id"
        private const val COLUMN_VIDEO_NAME = "name"
        private const val COLUMN_VIDEO_DURATION = "duration"
        private const val COLUMN_VIDEO_PUBLICATION_DATE = "publication_date"
        private const val COLUMN_VIDEO_QUALITY = "quality"
        private const val COLUMN_VIDEO_LANGUAGE = "language"
        const val COLUMN_PLATFORM_ID_FK = "platform_id"
    }

    fun addVideoToPlatform(name: String, duration: String, publicationDate: String, quality: String, language: String, platformId: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_VIDEO_NAME, name)
            put(COLUMN_VIDEO_DURATION, duration)
            put(COLUMN_VIDEO_PUBLICATION_DATE, publicationDate)
            put(COLUMN_VIDEO_QUALITY, quality)
            put(COLUMN_VIDEO_LANGUAGE, language)
            put(COLUMN_PLATFORM_ID_FK, platformId)
        }
        return db.insert(TABLE_VIDEOS, null, values)
    }


    override fun onCreate(db: SQLiteDatabase) {
        val createPlatformsTable = "CREATE TABLE $TABLE_PLATFORMS (" +
                "$COLUMN_PLATFORM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_PLATFORM_NAME TEXT, " +
                "$COLUMN_PLATFORM_PRICE REAL, " +
                "$COLUMN_PLATFORM_VIDEO_QUALITY TEXT, " +
                "$COLUMN_PLATFORM_RELATED_PLATFORMS TEXT, " +
                "$COLUMN_PLATFORM_AUDIENCE INTEGER)"

        val createVideosTable = "CREATE TABLE $TABLE_VIDEOS (" +
                "$COLUMN_VIDEO_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_VIDEO_NAME TEXT, " +
                "$COLUMN_VIDEO_DURATION TEXT, " +
                "$COLUMN_VIDEO_PUBLICATION_DATE TEXT, " +
                "$COLUMN_VIDEO_QUALITY TEXT, " +
                "$COLUMN_VIDEO_LANGUAGE TEXT, " +
                "$COLUMN_PLATFORM_ID_FK INTEGER, " +
                "FOREIGN KEY($COLUMN_PLATFORM_ID_FK) REFERENCES $TABLE_PLATFORMS($COLUMN_PLATFORM_ID))"
        db.execSQL(createPlatformsTable)
        db.execSQL(createVideosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VIDEOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLATFORMS")
        onCreate(db)
    }

    // Métodos para manejar las plataformas y videos
    fun addPlatform(name: String, price: Double, videoQuality: String, relatedPlatforms: String, audience: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLATFORM_NAME, name)
            put(COLUMN_PLATFORM_PRICE, price)
            put(COLUMN_PLATFORM_VIDEO_QUALITY, videoQuality)
            put(COLUMN_PLATFORM_RELATED_PLATFORMS, relatedPlatforms)
            put(COLUMN_PLATFORM_AUDIENCE, audience)
        }
        return db.insert(TABLE_PLATFORMS, null, values)
    }

    fun getAllPlatforms(): List<Map<String, Any>> {
        val db = this.readableDatabase
        val platforms = mutableListOf<Map<String, Any>>()
        val cursor = db.query(
            TABLE_PLATFORMS,
            null, // Obtén todas las columnas
            null,
            null,
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                platforms.add(mapOf(
                    "id" to it.getLong(it.getColumnIndexOrThrow(COLUMN_PLATFORM_ID)),
                    "name" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_NAME)),
                    "price" to it.getDouble(it.getColumnIndexOrThrow(COLUMN_PLATFORM_PRICE)),
                    "videoQuality" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_VIDEO_QUALITY)),
                    "relatedPlatforms" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_RELATED_PLATFORMS)),
                    "audience" to it.getInt(it.getColumnIndexOrThrow(COLUMN_PLATFORM_AUDIENCE))
                ))
            }
        }
        return platforms
    }

    fun updatePlatform(id: Long, name: String, price: Double, videoQuality: String, relatedPlatforms: String, audience: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLATFORM_NAME, name)
            put(COLUMN_PLATFORM_PRICE, price)
            put(COLUMN_PLATFORM_VIDEO_QUALITY, videoQuality)
            put(COLUMN_PLATFORM_RELATED_PLATFORMS, relatedPlatforms)
            put(COLUMN_PLATFORM_AUDIENCE, audience)
        }
        val rowsAffected = db.update(TABLE_PLATFORMS, values, "$COLUMN_PLATFORM_ID = ?", arrayOf(id.toString()))
        return rowsAffected > 0
    }



    fun getVideosForPlatform(platformId: Long): List<Map<String, Any>> {
        val db = this.readableDatabase
        val videoList = mutableListOf<Map<String, Any>>()
        val cursor = db.query(
            TABLE_VIDEOS,
            null,
            "$COLUMN_PLATFORM_ID_FK = ?",
            arrayOf(platformId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                videoList.add(mapOf(
                    "id" to it.getLong(it.getColumnIndexOrThrow(COLUMN_VIDEO_ID)),
                    "name" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_NAME)),
                    "duration" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_DURATION)),
                    "publicationDate" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_PUBLICATION_DATE)),
                    "quality" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_QUALITY)),
                    "language" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_LANGUAGE))
                ))
            }
        }
        return videoList
    }
    fun getPlatformById(id: Long): Map<String, Any>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PLATFORMS,
            null,
            "$COLUMN_PLATFORM_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return mapOf(
                    "id" to it.getLong(it.getColumnIndexOrThrow(COLUMN_PLATFORM_ID)),
                    "name" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_NAME)),
                    "price" to it.getDouble(it.getColumnIndexOrThrow(COLUMN_PLATFORM_PRICE)),
                    "videoQuality" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_VIDEO_QUALITY)),
                    "relatedPlatforms" to it.getString(it.getColumnIndexOrThrow(COLUMN_PLATFORM_RELATED_PLATFORMS)),
                    "audience" to it.getInt(it.getColumnIndexOrThrow(COLUMN_PLATFORM_AUDIENCE))
                )
            }
        }
        return null
    }

    // Operaciones con Videos
    fun updateVideo(id: Long, name: String, duration: String, publicationDate: String, quality: String, language: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_VIDEO_NAME, name)
            put(COLUMN_VIDEO_DURATION, duration)
            put(COLUMN_VIDEO_PUBLICATION_DATE, publicationDate)
            put(COLUMN_VIDEO_QUALITY, quality)
            put(COLUMN_VIDEO_LANGUAGE, language)
        }
        return db.update(TABLE_VIDEOS, values, "$COLUMN_VIDEO_ID = ?", arrayOf(id.toString())) > 0
    }

    fun deleteVideo(id: Long): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_VIDEOS, "$COLUMN_VIDEO_ID = ?", arrayOf(id.toString())) > 0
    }

    fun getVideoById(videoId: Long): Map<String, Any> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_VIDEOS,
            null,
            "$COLUMN_VIDEO_ID = ?",
            arrayOf(videoId.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                return mapOf(
                    "id" to it.getLong(it.getColumnIndexOrThrow(COLUMN_VIDEO_ID)),
                    "name" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_NAME)),
                    "duration" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_DURATION)),
                    "publicationDate" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_PUBLICATION_DATE)),
                    "quality" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_QUALITY)),
                    "language" to it.getString(it.getColumnIndexOrThrow(COLUMN_VIDEO_LANGUAGE))
                )
            }
        }
        throw IllegalArgumentException("Video with ID $videoId not found.")
    }

    // VideoDatabaseHelper.kt
    fun addVideoToPlatform(title: String, duration: Double, publicationDate: String, quality: String, language: String, platformId: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_VIDEO_NAME, title)
            put(COLUMN_VIDEO_DURATION, duration)
            put(COLUMN_VIDEO_PUBLICATION_DATE, publicationDate)
            put(COLUMN_VIDEO_QUALITY, quality)
            put(COLUMN_VIDEO_LANGUAGE, language)
            put(COLUMN_PLATFORM_ID_FK, platformId)
        }
        return db.insert(TABLE_VIDEOS, null, values)
    }


}
