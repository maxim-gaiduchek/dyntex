package cz.cvut.fit.sp1.api.exception.mediaExceptions.mask

import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes
import cz.cvut.fit.sp1.api.exception.mediaExceptions.MaskException

class MediaFileIsNotMaskException (
    exceptionCode: ExceptionCodes,
    vararg formatArgs: Any?
) : MaskException(exceptionCode, formatArgs)