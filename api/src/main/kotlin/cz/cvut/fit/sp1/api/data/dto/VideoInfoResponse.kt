package cz.cvut.fit.sp1.api.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class VideoInfoResponse {
    val fps: String = ""
    val message: String = ""
    val output_file: String = ""
    val success: Boolean = false
    val height: Int = 0
    val duration: Double = 0.0
    val preview: String = ""
    val size: String = ""
    val width: Int = 0
}
