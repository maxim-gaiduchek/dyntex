package cz.cvut.fit.sp1.api.data.model.media

import jakarta.persistence.Entity

@Entity
class Video(
    name: String,
    path: String,
    format: String,
) : Media(name, path, format) {
    var duration: Double = 0.0
    var fps: Double = 0.0
    var cameraMotion: Boolean = false
    var previewPath: String = ""
}
