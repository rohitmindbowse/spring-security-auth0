package com.security.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.security.entity.User;
import com.security.exception.CustomException;
import com.security.repo.UserRepo;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserRepo userRepo;

	/**
	 * User by email
	 * 
	 * @author rohit.kavthekar@mindbowser.com
	 * @param email
	 * @return {@link User}
	 */
	@Override
	public User userByEmail(String email) {

		return userRepo.findByEmail(email)
				.orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND.value()));
	}
}
