package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class VideoExceptionCodes(
        override val code: String,
        override val description: String
) : ExceptionCodes {

    VIDEO_NOT_FOUND("DTEX-VID-001", "Video with id %d is not found")
}