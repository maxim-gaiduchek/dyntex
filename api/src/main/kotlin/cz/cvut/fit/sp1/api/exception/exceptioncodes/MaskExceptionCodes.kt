package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class MaskExceptionCodes (
    override val code: String,
    override val description: String
) : ExceptionCodes {

    INVALID_MASK_FILE("DTEX-MASK-001", "Excepted mask file in format .png, but don't receive it"),
    INVALID_MASK_ID("DTEX-MASK-002", "Video with that ID don't exist"),
}