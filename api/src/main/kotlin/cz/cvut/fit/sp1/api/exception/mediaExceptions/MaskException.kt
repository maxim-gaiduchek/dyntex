package cz.cvut.fit.sp1.api.exception.mediaExceptions

import cz.cvut.fit.sp1.api.exception.AbstractException
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes

open class MaskException (
    exceptionCode: ExceptionCodes,
    vararg formatArgs: Any?
) : AbstractException(exceptionCode, formatArgs)