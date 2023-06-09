package com.security.exception;

import lombok.Getter;

/**
 * Common exception thrown from service
 * @author rohit.kumar@mindbowser.com
 *
 */
@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final Integer errorCode;

	public CustomException(String message, Throwable e, Integer errorCode) {
		super(message, e);
		this.errorCode = errorCode;
	}

	public CustomException(String message, Integer errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
