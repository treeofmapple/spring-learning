package com.tom.first.library.common;

import java.time.LocalDate;

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.UserRequest;
import com.tom.first.library.dto.UserRequest.PasswordRequest;
import com.tom.first.library.model.Book;
import com.tom.first.library.model.BookItem;
import com.tom.first.library.model.User;

public class SystemUtils {

	protected void mergeUser(User user, UserRequest request) {
		user.setUsername(request.username());
		user.setAge(request.age());
		user.setEmail(request.email());
		if (request.password() != null) {
			user.setPassword(request.password());
		}
	}

	protected void mergePassword(User user, PasswordRequest request) {
		if (request.password() != null) {
			user.setPassword(request.password());
		}
	}

	protected void mergeBook(Book book, BookRequest request) {
		book.setTitle(request.title());
		book.setAuthor(request.author());
		book.setQuantity(request.quantity());
		book.setPrice(request.price());
		book.setLaunchYear(request.launchYear());
	}

	protected void mergeItem(BookItem item, User user, Book book) {
		item.setBook(book);
		item.setUser(user);
	}
	
	protected void rentDate(BookItem itemBook) {
		itemBook.setRentStart(LocalDate.now());
		itemBook.setRentEnd(LocalDate.now().plusDays(30));
	}
}
