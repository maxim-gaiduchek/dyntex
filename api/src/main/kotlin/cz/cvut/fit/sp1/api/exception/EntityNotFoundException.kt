package cz.cvut.fit.sp1.api.exception

import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes

class EntityNotFoundException(
        exceptionCode: ExceptionCodes,
        vararg formatArgs: Any?
) : AbstractException(exceptionCode, formatArgs)