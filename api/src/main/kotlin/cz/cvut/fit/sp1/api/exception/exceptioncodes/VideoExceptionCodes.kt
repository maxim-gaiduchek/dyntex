package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class VideoExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    VIDEO_NOT_FOUND("DTEX-VID-001", "Video with id %d is not found"),
    INVALID_VIDEO_FILE("DTEX-VID-002", "Excepted video file, but don't receive it"),

    PYTHON_INFO_ERROR("DTEX-VID-003", "Python video info extraction fails"),
}
