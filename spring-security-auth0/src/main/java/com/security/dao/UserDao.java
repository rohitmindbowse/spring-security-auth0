package com.security.dao;

import com.security.entity.User;

public interface UserDao {

	/**
	 * User by email
	 * 
	 * @author rohit.kavthekar@mindbowser.com
	 * @param email
	 * @return {@link User}
	 */
	User userByEmail(String email);

}
