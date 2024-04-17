package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class ValidationExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    INVALID_DTO("DTEX-VLD-001", "Invalid DTO"),

    INVALID_TAG_ID("DTEX-VLD-001", "Invalid tag id %s"),
}
