package com.burntcity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD Operations for Account Entity.
 * 
 * @author Mike
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * Custom finder-method. It creates a JPA query of the form "select a from
	 * Account a where a.username = :username," run it (passing in the method
	 * argument username as a named parameter for the query), and return the
	 * results for us.
	 * 
	 * @param username
	 *            the username to match
	 * @return The account if found.
	 */
	Optional<Account> findByUsername(String username);
}
