package com.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuccessResponse<T> {

	private String message;
	private Integer code;
	private T data;
	private Boolean error;

}
