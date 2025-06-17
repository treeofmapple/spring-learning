package com.tom.first.library.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.dto.BookResponse.BookUpdateResponse;
import com.tom.first.library.dto.ItemResponse;
import com.tom.first.library.dto.ItemResponse.ItemBookResponse;
import com.tom.first.library.model.Book;
import com.tom.first.library.model.BookItem;
import com.tom.first.library.model.User;

@Service
public class BookMapper {

	public Book toBooks(BookRequest request) {
		if (request == null) {
			return null;
		}

		return Book.builder()
				.title(request.title())
				.author(request.author())
				.quantity(request.quantity())
				.price(request.price())
				.launchYear(request.launchYear())
				.build();
	}

	public BookResponse fromBook(Book book) {
		return new BookResponse(
				book.getTitle(), 
				book.getAuthor(), 
				book.getQuantity(), 
				book.getPrice(),
				book.getLaunchYear(), 
				book.getCreatedDate()
				);
	}
	
	public BookUpdateResponse fromUpdateResponse(Book book) {
		return new BookUpdateResponse(
				book.getTitle(),
				book.getAuthor(),
				book.getQuantity(),
				book.getPrice(),
				book.getLaunchYear()
				);
	}

	public BookItem toBookItem(Book book, User user) {
		if (book == null || user == null) {
			return null;
		}
		return BookItem.builder()
				.book(book)
				.user(user)
				.build();
	}

	public ItemResponse fromBookItem(BookItem item) {
		return new ItemResponse(
				new ItemResponse.BookDTO(item.getBook()), 
				new ItemResponse.UserDTO(item.getUser()), 
				item.getStatus(), 
				item.getRentStart(),
				item.getRentEnd()
				);
	}

	public ItemBookResponse fromBookItemUser(BookItem item) {
		return new ItemBookResponse(
				new ItemResponse.BookDTO(item.getBook()), 
				new ItemResponse.UserDTO(item.getUser()),
				item.getStatus()
				);
	}

	
}
