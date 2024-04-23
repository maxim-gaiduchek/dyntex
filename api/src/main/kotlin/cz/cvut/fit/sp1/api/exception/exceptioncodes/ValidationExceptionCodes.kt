package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class ValidationExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    INVALID_DTO("DTEX-VLD-001", "Invalid DTO"),
}
