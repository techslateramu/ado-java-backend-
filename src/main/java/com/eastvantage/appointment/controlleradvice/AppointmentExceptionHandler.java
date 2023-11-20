package com.eastvantage.appointment.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eastvantage.appointment.exception.AppointmentException;
import com.eastvantage.appointment.exception.AppointmentNotFoundException;
import com.eastvantage.appointment.response.FailureResponse;

@RestControllerAdvice
public class AppointmentExceptionHandler {

	@ExceptionHandler(value = AppointmentNotFoundException.class)
	public ResponseEntity<FailureResponse> handleException(AppointmentNotFoundException exception) {
		return new ResponseEntity<>(new FailureResponse(true, exception.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AppointmentException.class)
	public ResponseEntity<FailureResponse> handleException(AppointmentException exception) {
		return new ResponseEntity<>(new FailureResponse(true, exception.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<FailureResponse> customValidationErrorHandling(MethodArgumentNotValidException exception) {
		FailureResponse response = new FailureResponse();
		FieldError fieldError = exception.getFieldError();
		if (fieldError != null) {
			response.setError(true);
			response.setMessage(fieldError.getDefaultMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
	}
}
