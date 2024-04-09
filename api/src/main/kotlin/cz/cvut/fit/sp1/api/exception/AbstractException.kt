package cz.cvut.fit.sp1.api.exception

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes
import lombok.Data
import lombok.EqualsAndHashCode

@EqualsAndHashCode(callSuper = true)
@JsonIncludeProperties("code", "description")
@Data
open class AbstractException(exceptionCode: ExceptionCodes, vararg formatArgs: Any?) : RuntimeException() {

    internal val code: String = exceptionCode.code
    internal val description: String = exceptionCode.description.format(formatArgs)
}