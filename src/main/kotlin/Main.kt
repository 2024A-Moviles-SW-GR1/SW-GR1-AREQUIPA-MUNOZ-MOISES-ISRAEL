import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val videoPlatforms = loadPlatformsFromFile("platforms.txt") ?: mutableListOf()

    var exit = false
    while (!exit) {
        println("\nMenu Principal:")
        println("1. Crear Plataforma de Video")
        println("2. Seleccionar Plataforma de Video")
        println("3. Actualizar Plataforma de Video")
        println("4. Eliminar Plataforma de Video")
        println("5. Salir")
        print("Selecciona una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                createVideoPlatform(videoPlatforms)
                savePlatformsToFile(videoPlatforms, "platforms.txt")
            }
            2 -> {
                if (videoPlatforms.isEmpty()) {
                    println("No hay plataformas disponibles. Crea una primero.")
                } else {
                    selectVideoPlatform(videoPlatforms)
                }
            }
            3 -> {
                if (videoPlatforms.isEmpty()) {
                    println("No hay plataformas disponibles. Crea una primero.")
                } else {
                    updateVideoPlatform(videoPlatforms)
                    savePlatformsToFile(videoPlatforms, "platforms.txt")
                }
            }
            4 -> {
                if (videoPlatforms.isEmpty()) {
                    println("No hay plataformas disponibles. Crea una primero.")
                } else {
                    deleteVideoPlatform(videoPlatforms)
                    savePlatformsToFile(videoPlatforms, "platforms.txt")
                }
            }
            5 -> {
                savePlatformsToFile(videoPlatforms, "platforms.txt")
                exit = true
            }
            else -> println("Opción no válida, por favor intenta de nuevo.")
        }
    }
}

fun createVideoPlatform(videoPlatforms: MutableList<VideoPlatform>) {
    print("Ingrese el nombre de la plataforma: ")
    val name = readLine() ?: ""
    print("Ingrese el total de videos: ")
    val totalVideos = readLine()?.toIntOrNull() ?: 0
    print("¿Número de cuentas por suscripción?: ")
    val numeroDeCuentasPorSuscripcion = readLine()?.toIntOrNull() ?: 0
    print("Ingrese el precio de la suscripción: ")
    val subscriptionFee = readLine()?.toDoubleOrNull() ?: 0.0
    print("Ingrese la fecha de creación (yyyy-MM-dd): ")
    val creationDate = SimpleDateFormat("yyyy-MM-dd").parse(readLine() ?: "2024-01-01")

    val videoPlatform = VideoPlatform(
        name = name,
        totalVideos = totalVideos,
        numeroCuentas = numeroDeCuentasPorSuscripcion,
        subscriptionFee = subscriptionFee,
        creationDate = creationDate
    )
    videoPlatforms.add(videoPlatform)
    println("Plataforma de video '$name' creada exitosamente.")
}

fun selectVideoPlatform(videoPlatforms: MutableList<VideoPlatform>) {
    println("Plataformas disponibles:")
    videoPlatforms.forEachIndexed { index, platform -> println("$index. ${platform.name}") }

    print("Seleccione el índice de la plataforma: ")
    val index = readLine()?.toIntOrNull()

    if (index != null && index in videoPlatforms.indices) {
        val videoPlatform = videoPlatforms[index]
        videoPlatformMenu(videoPlatform)
    } else {
        println("Índice no válido.")
    }
}

fun videoPlatformMenu(videoPlatform: VideoPlatform) {
    var exit = false
    while (!exit) {
        println("\nMenu de ${videoPlatform.name}:")
        println("1. Crear Video")
        println("2. Leer Videos desde el archivo")
        println("3. Actualizar Video")
        println("4. Eliminar Video")
        println("5. Volver al menu principal")
        print("Selecciona una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                createVideo(videoPlatform)
                saveVideosToFile(videoPlatform.videos, "${videoPlatform.name}_videos.txt")
            }
            2 -> {
                val videos = loadVideosFromFile("${videoPlatform.name}_videos.txt")
                if (videos != null && videos.isNotEmpty()) {
                    videoPlatform.videos = videos
                    videoPlatform.totalVideos = videos.size
                    println("Videos cargados:\n${videos.joinToString(separator = "\n")}")
                } else {
                    println("Aún no se han guardado videos dentro de la plataforma.")
                }
            }
            3 -> {
                val videos = loadVideosFromFile("${videoPlatform.name}_videos.txt")
                if (videos != null) {
                    videoPlatform.videos = videos
                    updateVideo(videoPlatform)
                    saveVideosToFile(videoPlatform.videos, "${videoPlatform.name}_videos.txt")
                } else {
                    println("No se pudieron cargar los videos para actualizar.")
                }
            }
            4 -> {
                val videos = loadVideosFromFile("${videoPlatform.name}_videos.txt")
                if (videos != null) {
                    videoPlatform.videos = videos
                    deleteVideo(videoPlatform)
                    saveVideosToFile(videoPlatform.videos, "${videoPlatform.name}_videos.txt")
                } else {
                    println("No se pudieron cargar los videos para eliminar.")
                }
            }
            5 -> exit = true
            else -> println("Opción no válida, por favor intenta de nuevo.")
        }
    }
}

fun updateVideoPlatform(videoPlatforms: MutableList<VideoPlatform>) {
    println("Plataformas disponibles:")
    videoPlatforms.forEachIndexed { index, platform -> println("$index. ${platform.name}") }

    print("Seleccione el índice de la plataforma a actualizar: ")
    val index = readLine()?.toIntOrNull()

    if (index != null && index in videoPlatforms.indices) {
        val videoPlatform = videoPlatforms[index]
        print("Nuevo nombre (actual: ${videoPlatform.name}): ")
        videoPlatform.name = readLine() ?: videoPlatform.name
        print("Nuevo total de videos (actual: ${videoPlatform.totalVideos}): ")
        videoPlatform.totalVideos = readLine()?.toIntOrNull() ?: videoPlatform.totalVideos
        print("Nuevo número de cuentas por suscripción (actual: ${videoPlatform.numeroCuentas}): ")
        videoPlatform.numeroCuentas = readLine()?.toIntOrNull() ?: videoPlatform.numeroCuentas
        print("Nuevo precio de suscripción (actual: ${videoPlatform.subscriptionFee}): ")
        videoPlatform.subscriptionFee = readLine()?.toDoubleOrNull() ?: videoPlatform.subscriptionFee
        print("Nueva fecha de creación (actual: ${SimpleDateFormat("yyyy-MM-dd").format(videoPlatform.creationDate)}): ")
        videoPlatform.creationDate = SimpleDateFormat("yyyy-MM-dd").parse(readLine() ?: SimpleDateFormat("yyyy-MM-dd").format(videoPlatform.creationDate))
        println("Plataforma de video actualizada exitosamente.")
    } else {
        println("Índice no válido.")
    }
}

fun deleteVideoPlatform(videoPlatforms: MutableList<VideoPlatform>) {
    println("Plataformas disponibles:")
    videoPlatforms.forEachIndexed { index, platform -> println("$index. ${platform.name}") }

    print("Seleccione el índice de la plataforma a eliminar: ")
    val index = readLine()?.toIntOrNull()

    if (index != null && index in videoPlatforms.indices) {
        videoPlatforms.removeAt(index)
        println("Plataforma de video eliminada exitosamente.")
    } else {
        println("Índice no válido.")
    }
}


