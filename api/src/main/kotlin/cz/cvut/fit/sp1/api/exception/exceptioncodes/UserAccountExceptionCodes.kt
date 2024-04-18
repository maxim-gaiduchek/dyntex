package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class UserAccountExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    USER_NOT_FOUND("DTEX-USR-001", "User with id %d not found"),
    USER_EMAIL_ALREADY_EXISTS("DTEX-USR-002", "User email %s already exists"),
    USER_PASSWORD_IS_EMPTY("DTEX-USR-003", "User password is empty"),
    USER_ACCESS_DENIED("DTEX-USR-004", "User access denied"),
    USER_WITH_AUTH_TOKEN_NOT_FOUND("DTEX-USR-005", "User with auth token %s doesn't exist"),
    AUTH_TOKEN_IS_EMPTY("DTEX-USR-006", "Auth token is empty"),

}
