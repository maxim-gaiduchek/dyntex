package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class ValidationExceptionCodes(
        override val code: String,
        override val description: String
) : ExceptionCodes {

    INVALID_DTO("DTEX-VLD-001", "Invalid DTO"),
    INVALID_VIDEO_FILE("DTEX-VLD-002", "Excepted video file, but don't receive it"),
    INVALID_MASK_FILE("DTEX-VLD-003", "Excepted mask file in format .png, but don't receive it")
}