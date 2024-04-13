package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class MaskExceptionCodes(
        override val code: String,
        override val description: String
) : ExceptionCodes {

    MASK_NOT_FOUND("DTEX-MSK-001", "Mask with id %d not found"),
    INVALID_MASK_FILE("DTEX-MSK-002", "Excepted mask file in format .png, but don't receive it");
}