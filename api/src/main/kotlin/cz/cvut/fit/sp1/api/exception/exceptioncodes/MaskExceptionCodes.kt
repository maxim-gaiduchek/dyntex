package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class MaskExceptionCodes (
    override val code: String,
    override val description: String
) : ExceptionCodes {

    INVALID_MASK_FILE("DTEX-MSK-001", "Excepted mask file in format .png, but don't receive it"),
    MASK_DOES_NOT_EXIST("DTEX-MSK-002", "Mask with id %d don't exist"),
}