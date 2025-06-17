package com.tom.first.library.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.library.dto.UserRequest;
import com.tom.first.library.dto.UserResponse;
import com.tom.first.library.dto.UserResponse.UserBookItems;
import com.tom.first.library.dto.UserResponse.UserUpdateResponse;
import com.tom.first.library.model.User;

@Service
public class UserMapper {

	public User toUser(UserRequest request) {
		if (request == null) {
			return null;
		}
		return User.builder()
				.username(request.username())
				.email(request.email())
				.password(request.password())
				.age(request.age())
				.build();
	}

	public UserResponse fromUser(User user) {
		return new UserResponse(
				user.getUsername(), 
				user.getEmail(), 
				user.getAge(), 
				user.getCreatedDate(),
				UserBookItems.fromBookItems(user.getBookItems())
				);
	}
	
	public UserUpdateResponse fromUpdateResponse(User user) {
		return new UserUpdateResponse(
				user.getUsername(), 
				user.getEmail(), 
				user.getAge()
				); 
	}

}
