package com.burntcity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookmarkNotFoundException extends RuntimeException{

	/**
	 * The bookmark Id was not found for the specified user.
	 */
	public BookmarkNotFoundException(String userId, Long bookmarkId) {
		super("could not find bookmark '" + bookmarkId + "' for '" + userId + "'.");
	}

	/**
	 * The bookmark does not exist in this system.
	 * 
	 * @param bookmarkId Id was not found in the system.
	 */
	public BookmarkNotFoundException(Long bookmarkId) {
		super("could not find bookmark '" + bookmarkId + "'.");
	}
}
