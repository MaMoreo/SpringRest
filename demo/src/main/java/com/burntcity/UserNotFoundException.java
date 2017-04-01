package com.burntcity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	/**
	 * The user id was not found.
	 */
	public UserNotFoundException(String userId) {
		super("could not find user '" + userId + "'.");
	}
}
