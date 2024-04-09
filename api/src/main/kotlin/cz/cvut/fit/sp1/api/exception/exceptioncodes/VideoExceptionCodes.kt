package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class VideoExceptionCodes(
    override val code: String,
    override val description: String
) : ExceptionCodes {

    INVALID_VIDEO_FILE("DTEX-VIDEO-001", "Excepted video file, but don't receive it"),
    INVALID_VIDEO_ID("DTEX-VIDEO-002", "Video with that ID don't exist")
}