package cz.cvut.fit.sp1.api.exception.mediaExceptions.video

import cz.cvut.fit.sp1.api.exception.exceptioncodes.ExceptionCodes
import cz.cvut.fit.sp1.api.exception.mediaExceptions.VideoException

class VideoNotFoundException (
    exceptionCode: ExceptionCodes,
    vararg formatArgs: Any?
) : VideoException(exceptionCode, formatArgs)