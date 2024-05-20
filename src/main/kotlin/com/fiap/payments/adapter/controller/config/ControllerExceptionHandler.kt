package com.fiap.payments.adapter.controller.config

import com.fiap.payments.domain.errors.ErrorType
import com.fiap.payments.domain.errors.PaymentsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(PaymentsException::class)
    protected fun domainErrorHandler(domainException: PaymentsException): ResponseEntity<ApiError> {
        val apiErrorResponseEntity: ApiErrorResponseEntity =
            when (domainException.errorType) {
                ErrorType.PAYMENT_NOT_FOUND,
                ->
                    ApiErrorResponseEntity(
                        ApiError(domainException.errorType.name, domainException.message),
                        HttpStatus.NOT_FOUND,
                    )
                
                ErrorType.INVALID_PAYMENT_STATE_TRANSITION,
                ->
                    ApiErrorResponseEntity(
                        ApiError(domainException.errorType.name, domainException.message),
                        HttpStatus.BAD_REQUEST,
                    )

                else ->
                    ApiErrorResponseEntity(
                        ApiError(ErrorType.UNEXPECTED_ERROR.name, domainException.localizedMessage),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                    )
            }
        return ResponseEntity.status(apiErrorResponseEntity.status).body(apiErrorResponseEntity.body)
    }

    data class ApiError(val error: String, val message: String?)

    data class ApiErrorResponseEntity(val body: ApiError, val status: HttpStatus)
}
