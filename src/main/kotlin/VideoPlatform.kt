import java.io.*
import java.text.SimpleDateFormat
import java.util.*

data class Video(
    var title: String,
    var duration: Int,
    var published: Boolean,
    var views: Int,
    var uploadDate: Date
)

data class VideoPlatform(
    var name: String,
    var totalVideos: Int,
    var numeroCuentas: Int,
    var subscriptionFee: Double,
    var creationDate: Date,
    var videos: MutableList<Video> = mutableListOf()
)

fun createVideo(videoPlatform: VideoPlatform) {
    print("Ingrese el título del video: ")
    val title = readLine() ?: ""
    print("Ingrese la duración del video (en minutos): ")
    val duration = readLine()?.toIntOrNull() ?: 0
    print("¿El video está publicado? (true/false): ")
    val published = readLine()?.toBoolean() ?: false
    print("Ingrese el número de vistas: ")
    val views = readLine()?.toIntOrNull() ?: 0
    print("Ingrese la fecha de subida (yyyy-MM-dd): ")
    val uploadDate = SimpleDateFormat("yyyy-MM-dd").parse(readLine() ?: "2024-01-01")

    val newVideo = Video(
        title = title,
        duration = duration,
        published = published,
        views = views,
        uploadDate = uploadDate
    )
    videoPlatform.videos.add(newVideo)
    videoPlatform.totalVideos = videoPlatform.videos.size
    println("Video creado exitosamente.")
}

fun updateVideo(videoPlatform: VideoPlatform) {
    if (videoPlatform.videos.isEmpty()) {
        println("No hay videos para actualizar.")
        return
    }

    println("Lista de videos:")
    videoPlatform.videos.forEachIndexed { index, video -> println("$index. ${video.title}") }

    print("Seleccione el índice del video a actualizar: ")
    val index = readLine()?.toIntOrNull()

    if (index != null && index in videoPlatform.videos.indices) {
        val video = videoPlatform.videos[index]
        print("Nuevo título (actual: ${video.title}): ")
        video.title = readLine() ?: video.title
        print("Nueva duración (actual: ${video.duration}): ")
        video.duration = readLine()?.toIntOrNull() ?: video.duration
        print("¿El video está publicado? (actual: ${video.published}): ")
        video.published = readLine()?.toBoolean() ?: video.published
        print("Nuevo número de vistas (actual: ${video.views}): ")
        video.views = readLine()?.toIntOrNull() ?: video.views
        print("Nueva fecha de subida (actual: ${SimpleDateFormat("yyyy-MM-dd").format(video.uploadDate)}): ")
        video.uploadDate = SimpleDateFormat("yyyy-MM-dd").parse(readLine() ?: SimpleDateFormat("yyyy-MM-dd").format(video.uploadDate))
        println("Video actualizado exitosamente.")
    } else {
        println("Índice no válido.")
    }
}

fun deleteVideo(videoPlatform: VideoPlatform) {
    if (videoPlatform.videos.isEmpty()) {
        println("No hay videos para eliminar.")
        return
    }

    println("Lista de videos:")
    videoPlatform.videos.forEachIndexed { index, video -> println("$index. ${video.title}") }

    print("Seleccione el índice del video a eliminar: ")
    val index = readLine()?.toIntOrNull()

    if (index != null && index in videoPlatform.videos.indices) {
        videoPlatform.videos.removeAt(index)
        videoPlatform.totalVideos = videoPlatform.videos.size
        println("Video eliminado exitosamente.")
    } else {
        println("Índice no válido.")
    }
}

fun saveVideosToFile(videos: List<Video>, filename: String) {
    BufferedWriter(FileWriter(filename)).use { writer ->
        for (video in videos) {
            writer.write("${video.title},${video.duration},${video.published},${video.views},${SimpleDateFormat("yyyy-MM-dd").format(video.uploadDate)}")
            writer.newLine()
        }
    }
}

fun loadVideosFromFile(filename: String): MutableList<Video>? {
    return try {
        val videos = mutableListOf<Video>()
        val file = File(filename)
        if (file.exists()) {
            BufferedReader(FileReader(filename)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    val parts = line.split(",")
                    val title = parts[0]
                    val duration = parts[1].toInt()
                    val published = parts[2].toBoolean()
                    val views = parts[3].toInt()
                    val uploadDate = SimpleDateFormat("yyyy-MM-dd").parse(parts[4])
                    val video = Video(title, duration, published, views, uploadDate)
                    videos.add(video)
                    line = reader.readLine()
                }
            }
        }
        if (videos.isEmpty()) null else videos
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun savePlatformsToFile(platforms: List<VideoPlatform>, filename: String) {
    BufferedWriter(FileWriter(filename)).use { writer ->
        platforms.forEach { platform ->
            writer.write("${platform.name},${platform.totalVideos},${platform.numeroCuentas},${platform.subscriptionFee},${SimpleDateFormat("yyyy-MM-dd").format(platform.creationDate)}")
            writer.newLine()
        }
    }
}

fun loadPlatformsFromFile(filename: String): MutableList<VideoPlatform>? {
    return try {
        val platforms = mutableListOf<VideoPlatform>()
        val file = File(filename)
        if (file.exists()) {
            BufferedReader(FileReader(filename)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    val parts = line.split(",")
                    val name = parts[0]
                    val totalVideos = parts[1].toInt()
                    val numeroCuentas = parts[2].toInt()
                    val subscriptionFee = parts[3].toDouble()
                    val creationDate = SimpleDateFormat("yyyy-MM-dd").parse(parts[4])
                    val platform = VideoPlatform(name, totalVideos, numeroCuentas, subscriptionFee, creationDate)
                    platforms.add(platform)
                    line = reader.readLine()
                }
            }
        }
        platforms
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}