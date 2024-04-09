package cz.cvut.fit.sp1.api.exception

import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes

open class MediaException (
    exceptionCode: ExceptionCodes,
    vararg formatArgs: Any?
) : AbstractException(exceptionCode, formatArgs)