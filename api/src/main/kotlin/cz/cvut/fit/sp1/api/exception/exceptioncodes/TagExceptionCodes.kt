package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class TagExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {

    TAG_NOT_FOUND("DTEX-TAG-001", "Bro))) there is no tag with id: %d"),
    TAG_IS_USED("DTEX-TAG-002", "Tag with id %d is used in other media"),
    TAG_NAME_EXISTS("DTEX-TAG-003", "Tag with name %s already exists"),
}
