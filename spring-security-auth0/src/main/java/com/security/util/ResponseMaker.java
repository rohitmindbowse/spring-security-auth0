package com.security.util;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.security.exception.ValidatioErrorResponse;
import com.security.model.ErrorResponse;
import com.security.model.SuccessResponse;

@Component
public class ResponseMaker {

	/**
	 * Custom success response object
	 * 
	 * @author Mindbowser | rohit.kavthekar@mindbowser.com Created Date: 09-Dec-2020
	 *         6:22:37 pm
	 * @param <T>
	 * @param message
	 * @param data
	 * @return {@link SuccessResponse}
	 */
	public <T> ResponseEntity<SuccessResponse<T>> successResponse(String message, T data) {

		SuccessResponse<T> response = new SuccessResponse<>(message, HttpStatus.OK.value(), data, false);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Custom error response object when any exception occurs in application
	 * 
	 * @author Mindbowser | rohit.kavthekar@mindbowser.com Created Date: 09-Dec-2020
	 *         6:22:29 pm
	 * @param message
	 * @param errorCode
	 * @return {@link ErrorResponse}
	 */
	public ResponseEntity<ErrorResponse> errorResponse(String message, Integer errorCode) {

		List<ValidatioErrorResponse> validationErros = null;
		ErrorResponse response = new ErrorResponse(message, errorCode, true, new Date(), validationErros);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Custom validation error response object when validations failed
	 * 
	 * @author Mindbowser | rohit.kavthekar@mindbowser.com Created Date: 09-Dec-2020
	 *         6:22:23 pm
	 * @param message
	 * @param errorCode
	 * @param validationErros
	 * @return {@link ErrorResponse}
	 */
	public ResponseEntity<Object> validationErrorResponse(String message, Integer errorCode,
			List<ValidatioErrorResponse> validationErros) {

		ErrorResponse response = new ErrorResponse(message, errorCode, true, new Date(), validationErros);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
