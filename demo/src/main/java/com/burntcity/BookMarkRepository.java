package com.burntcity;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * CRUD operations for Bookmarks
 */
public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

	/**
	 * Finder method that dereferences the username property 
	 * on the Bookmark entity’s Account relationship, 
	 * ultimately requiring a join of some sort. 
	 * 
	 * The JPA query it generates is, roughly: 
	 * SELECT b from Bookmark b WHERE b.account.username = :username.
	 * 
	 * @param username username in Account to be found
	 * @return A collection with all the accouts for this user.
	 */
	Collection<Bookmark> findByAccountUsername(String username);
}
