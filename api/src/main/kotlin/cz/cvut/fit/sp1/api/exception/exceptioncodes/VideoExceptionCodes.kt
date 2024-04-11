package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class VideoExceptionCodes(
    override val code: String,
    override val description: String
) : ExceptionCodes {

    INVALID_VIDEO_FILE("DTEX-VID-001", "Excepted video file, but don't receive it"),
    VIDEO_DOES_NOT_EXIST("DTEX-VID-002", "Video with id %d don't exist"),
}