package com.burntcity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	/**
	 *  mappedBy indicates the entity is the
	 *  inverse of the
	 *  relationship
	 */
	@OneToMany(mappedBy = "account") 
	private Set<Bookmark> bookmarks = new HashSet<>(); 
														

	@JsonIgnore
	private String password;
	private String username;

	/**
	 * Default Constructor for JPA
	 */
	Account() {
		// JPA Only
	}

	public Account(String name, String password) {
		this.username = name;
		this.password = password;
	}

	public Set<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

}
