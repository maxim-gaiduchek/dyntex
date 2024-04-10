package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class MaskExceptionCodes(
        override val code: String,
        override val description: String
) : ExceptionCodes {

    MASK_NOT_FOUND("DTEX-MSK-001", "Mask with id %d is not found")
}