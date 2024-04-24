package cz.cvut.fit.sp1.api.data.model.media

import cz.cvut.fit.sp1.api.data.model.UserAccount
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany

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

    @ManyToMany(mappedBy = "likedVideos")
    var likedByUsers: MutableList<UserAccount> = mutableListOf()
}

