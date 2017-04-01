package com.burntcity;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/{userId}/bookmarks")
public class BookmarkRestController {

	private final BookMarkRepository bookmarkRepository;
	private final AccountRepository accountRepository;

	@Autowired
	public BookmarkRestController(BookMarkRepository bookMarkRepository, AccountRepository accountRepository) {
		this.bookmarkRepository = bookMarkRepository;
		this.accountRepository = accountRepository;
	}

	/**
	 * Gets all the bookmarks for the specified user account. Replies to
	 * /{userId}/bookmarks (GET)
	 * 
	 * @return A collection with all the bookmarks
	 */
	@RequestMapping(method = RequestMethod.GET)
	Collection<Bookmark> getAllBookmarksForUser(@PathVariable String userId) {
		this.validateUser(userId);
		return bookmarkRepository.findByAccountUsername(userId);
	}

	/**
	 * Answers to the path: /{userId}/bookmarks/{bookmarkId}
	 * 
	 * @param userId
	 * @param bookmarkId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{bookmarkId}")
	Bookmark getBookmarkForUser(@PathVariable String userId, @PathVariable Long bookmarkId) {
		this.validateUser(userId);
		Bookmark bookmarkToFind = validateBookmark(bookmarkId);

		if (findBookmarInAccount(userId, bookmarkToFind)) {
			return bookmarkToFind;
		} else {
			throw new BookmarkNotFoundException(userId, bookmarkId);
		}
	}

	/**
	 * Deletes one bookmark for an specified user.
	 * 
	 * Answers to the path: /{userId}/bookmarks/{bookmarkId}
	 * <br>
	 * Example of use:
	 * curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/miguel/bookmarks/19
	 * 
	 * @param userId
	 *            the user whom bookmark belogs
	 * @param bookmarkId
	 *            The bookmark to be deleted
	 * 
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{bookmarkId}")
	Object deleteBookmarksForUser(@PathVariable String userId, @PathVariable Long bookmarkId) {

		this.validateUser(userId);
		this.validateBookmark(bookmarkId);
		
		return this.accountRepository.findByUsername(userId).map(account -> {

			Bookmark result = bookmarkRepository.findOne(bookmarkId);
			bookmarkRepository.delete(result);
			return result;
		});
	}

	/**
	 * Creates a bookmark for an existing user. Replies to /{userId}/bookmarks
	 * (POST) <br>
	 * <br>
	 * 
	 * Example of curl call: <br>
	 * curl -H "Content-Type: application/json" -X POST -d
	 * {\"uri\":\"http://bookmark.com/1/miguel\"}
	 * http://localhost:8080/miguel/bookmarks <br>
	 * <br>
	 * curl -H "Content-Type: application/json" -X POST -d
	 * "{\"description\":\"nuevo aviso\"}"
	 * http://localhost:8080/miguel/bookmarks <br>
	 * 
	 * @param userId
	 * @param input
	 * @return the created entity if any
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> createBookmarksForUser(@PathVariable String userId, @RequestBody Bookmark input) {
		this.validateUser(userId);

		return this.accountRepository.findByUsername(userId).map(account -> {
			Bookmark result = bookmarkRepository.save(new Bookmark(account, input.uri, input.description));

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
					.toUri();

			return ResponseEntity.created(location).build();
		}).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * Finds a bookmark Id in the specified user account.
	 * 
	 * @param userName
	 * @param bookmarkToFind
	 * @return
	 */
	private Boolean findBookmarInAccount(String userName, Bookmark bookmarkToFind) {
		Collection<Bookmark> bookmarks = bookmarkRepository.findByAccountUsername(userName);
		Boolean found = false;
		for (Bookmark bookmark : bookmarks) {
			if (bookmark.getId() == bookmarkToFind.getId()) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Cheks if the user exists. If not, throws an Exception
	 * 
	 * @param userId
	 *            the userId, (normaly the name) to be checked.
	 */
	private void validateUser(String userId) {
		accountRepository.findByUsername(userId).orElseThrow(() -> new UserNotFoundException(userId));
	}

	/**
	 * Cheks if the bookmark exists in the system. If not, throws an Exception
	 * 
	 * @param bookmarkId
	 *            the id to be checked.
	 * @return the Bookmak if found, throws a {@link BookmarkNotFoundException}
	 *         otherwise
	 */
	private Bookmark validateBookmark(Long bookmarkId) {
		Bookmark findOne = this.bookmarkRepository.findOne(bookmarkId);
		if (findOne == null) {
			throw new BookmarkNotFoundException(bookmarkId);
		}
		return findOne;
	}
}
