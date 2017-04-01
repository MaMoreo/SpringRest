package com.burntcity;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Once started, Spring Boot will call all beans of type CommandLineRunner,
	 * giving them a callback.
	 * 
	 * In this case, CommandLineRunner is an interface with one abstract method,
	 * which means that - in the world of Java 8 - we can substitute its
	 * definition with a lambda expression. (the lambda syntax its like an
	 * anonymous inner class implementing the interface in question).
	 * 
	 * @param accountRepository
	 *            Spring will inject this automatically
	 * @param bookmarkRepository
	 *            Spring will inject this automatically
	 * @return
	 */
	@Bean
	CommandLineRunner init(AccountRepository accountRepository, BookMarkRepository bookmarkRepository) {
		// CommandLineRunner runner = (String [] evt) -> body of the run method
		// parameter of the run method -> body of the run method
		return (evt) -> Arrays.asList("steve,peter,bruce,clark,rwinch,mfisher,mpollack,jlong".split(",")).forEach(a -> {
			Account account = accountRepository.save(new Account(a, "password"));
			bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + a, "A description"));
			bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + a, "A description"));
		});
	}

	/**
	 * Inner Class (java 7) 
	 */
	@Bean
	CommandLineRunner run(AccountRepository accountRepository, BookMarkRepository bookmarkRepository) {
		CommandLineRunner c = new CommandLineRunner() {
			public void run(String... args) {
				String name = "miguel";
				Account myTestAccount = accountRepository.save(new Account(name, "secret"));
				bookmarkRepository.save(
						new Bookmark(myTestAccount, "http://bookmark.com/1/" + name, "First bookmark description"));
				bookmarkRepository.save(
						new Bookmark(myTestAccount, "http://bookmark.com/2/" + name, "Second bookmark description"));
				bookmarkRepository.save(
						new Bookmark(myTestAccount, "http://bookmark.com/3/" + name, "Third bookmark description"));
			}
		};
		return c;
	}
}
