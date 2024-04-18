package cz.cvut.fit.sp1.api.exception.exceptioncodes

enum class FileStoreExceptionCodes(
    override val code: String,
    override val description: String,
) : ExceptionCodes {
    READ_FILE_ERROR("DTEX-FST-001", "Can not obtain file %s"),
    STORE_FILE_ERROR("DTEX-FST-001", "Can not store file"),
}
