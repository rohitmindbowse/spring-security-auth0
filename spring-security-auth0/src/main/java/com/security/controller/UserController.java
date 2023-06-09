package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.AuthToken;
import com.security.model.SuccessResponse;
import com.security.util.ResponseMaker;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private ResponseMaker responseMaker;


	@GetMapping("test")
	public ResponseEntity<SuccessResponse<AuthToken>> testUser() {
		
		return responseMaker.successResponse("SUCC", null);
	}
	
	
}
