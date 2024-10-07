package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class AccessExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {

    INVALID_PASSWORD("SEC-AUTH-001", "Invalid password"),
    INVALID_JWT("SEC-AUTH-002", "Invalid JWT"),
    EXPIRED_JWT("SEC-AUTH-003", "JWT is expired"),
}