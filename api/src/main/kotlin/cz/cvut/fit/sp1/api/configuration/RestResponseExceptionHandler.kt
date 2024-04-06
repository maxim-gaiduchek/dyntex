package cz.cvut.fit.sp1.api.configuration

import cz.cvut.fit.sp1.api.exception.*
import cz.cvut.fit.sp1.api.exception.exceptioncodes.ValidationExceptionCodes
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
class RestResponseExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private const val STATUS_KEY = "status"
        private const val TIMESTAMP_KEY = "timestamp"
        private const val CODE_KEY = "code"
        private const val DESCRIPTION_KEY = "description"
    }

    @ExceptionHandler(Exception::class)
    protected fun exceptionHandler(exception: Exception): ResponseEntity<Any> {
        val body = LinkedHashMap<Any, Any>()
        return ResponseEntity(body, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ValidationException::class)
    protected fun exceptionHandler(exception: ValidationException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.BAD_REQUEST, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    protected fun exceptionHandler(exception: AccessDeniedException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.FORBIDDEN, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun exceptionHandler(exception: EntityNotFoundException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.NOT_FOUND, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AuthorizationException::class)
    protected fun exceptionHandler(exception: AuthorizationException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.UNAUTHORIZED, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MediaFileIsNotMaskException::class)
    protected fun exceptionHandler(exception: MediaFileIsNotMaskException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.NOT_ACCEPTABLE, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(MediaFileIsNotVideoException::class)
    protected fun exceptionHandler(exception: MediaFileIsNotVideoException): ResponseEntity<Any> {
        val body = getResponse(HttpStatus.NOT_ACCEPTABLE, exception)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.NOT_ACCEPTABLE)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                              headers: HttpHeaders,
                                              status: HttpStatusCode,
                                              request: WebRequest): ResponseEntity<Any> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach { error: ObjectError ->
            val fieldName = if (error is FieldError) {
                error.field
            } else {
                error.objectName
            }
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(getInvalidDtoResponse(errors), HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    private fun getResponse(status: HttpStatus, exception: AbstractException): Map<String, Any> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body[STATUS_KEY] = status.toString()
        body[TIMESTAMP_KEY] = Date()
        body[CODE_KEY] = exception.code
        body[DESCRIPTION_KEY] = exception.description
        return body
    }

    private fun getInvalidDtoResponse(errors: Map<String, String?>): Map<String, Any> {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body[STATUS_KEY] = HttpStatus.BAD_REQUEST.toString()
        body[TIMESTAMP_KEY] = Date()
        body[CODE_KEY] = ValidationExceptionCodes.INVALID_DTO.code
        body[DESCRIPTION_KEY] = ValidationExceptionCodes.INVALID_DTO.description
        body["errors"] = errors
        return body
    }
}