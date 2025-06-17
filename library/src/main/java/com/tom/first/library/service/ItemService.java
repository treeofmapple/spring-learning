package com.tom.first.library.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tom.first.library.common.SystemUtils;
import com.tom.first.library.dto.ItemRequest;
import com.tom.first.library.dto.ItemResponse;
import com.tom.first.library.dto.ItemResponse.ItemBookResponse;
import com.tom.first.library.dto.UserRequest.NameRequest;
import com.tom.first.library.exception.LimitException;
import com.tom.first.library.exception.NotFoundException;
import com.tom.first.library.mapper.BookMapper;
import com.tom.first.library.model.BookItem;
import com.tom.first.library.model.User;
import com.tom.first.library.model.enums.Status;
import com.tom.first.library.repository.BookRepository;
import com.tom.first.library.repository.ItemRepository;
import com.tom.first.library.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService extends SystemUtils {

	@Value("${application.limit.books}")
	private static int MAX_BOOKS;

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final BookMapper mapper;

	public List<ItemResponse> findAll() {
		List<BookItem> bookItems = itemRepository.findAll();
		if (bookItems.isEmpty()) {
			throw new NotFoundException("No books items found");
		}
		return bookItems.stream().map(mapper::fromBookItem).collect(Collectors.toList());
	}

	public List<ItemBookResponse> findItemByUser(NameRequest request) {
		User user = userRepository.findByUsername(request.username()).orElseThrow(
				() -> new NotFoundException(String.format("User with name %s was not found.", request.username())));

		List<BookItem> items = itemRepository.findByUser(user.getId());
		if (items.isEmpty()) {
			throw new NotFoundException(String.format("No book items found for user: %s.", request.username()));
		}
		return items.stream().map(mapper::fromBookItemUser).collect(Collectors.toList());
	}

	@Transactional
	public ItemBookResponse createItem(ItemRequest request) {

		var book = bookRepository.findByTitle(request.bookName())
				.orElseThrow(() -> new NotFoundException("Book not found with ID: " + request.bookName()));

		var user = userRepository.findByUsername(request.username())
				.orElseThrow(() -> new NotFoundException("User not found with ID: " + request.username()));

		if (itemRepository.countByUserIdAndBookIdAndStatus(user.getId(), book.getId(), Status.RENT) > MAX_BOOKS) {
			throw new LimitException("User reached max rent limit for book ID: " + book.getTitle());
		}

		var itemBook = mapper.toBookItem(book, user);
		itemBook.setStatus(Status.AVAILABLE);
		itemRepository.save(itemBook);
		return mapper.fromBookItemUser(itemBook);
	}

	@Transactional
	public ItemBookResponse updateItem(ItemRequest request) {
        var itemBook = itemRepository.findByBookName(request.bookName())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Cannot update BookItem, no bookItem found with title: %s", request.bookName())));

        itemBook.setStatus(Status.AVAILABLE);
        itemRepository.save(itemBook);
        return mapper.fromBookItemUser(itemBook);
	}

	@Transactional
	public ItemBookResponse startRent(ItemRequest request) {
        var itemBook = itemRepository.findByBookName(request.bookName())
                .orElseThrow(() -> new NotFoundException("BookItem not found with title: " + request.bookName()));

        itemBook.setStatus(Status.RENT);
        rentDate(itemBook);
        itemRepository.save(itemBook);
        return mapper.fromBookItemUser(itemBook);
	}

	@Transactional
	public void deleteItem(ItemRequest request) {
		
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("User not found with username: " + request.username()));

        var book = bookRepository.findByTitle(request.bookName())
                .orElseThrow(() -> new NotFoundException("Book not found with title: " + request.bookName()));

        if (!itemRepository.existsByBookAndUser(book, user)) {
            throw new NotFoundException("BookItem not found for user: " + request.username());
        }

        itemRepository.deleteByBookAndUser(book, user);
	}

}
