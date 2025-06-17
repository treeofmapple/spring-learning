package com.tom.first.username.common;

import org.springframework.stereotype.Service;

import com.tom.first.username.dto.UsernameRequest;
import com.tom.first.username.model.Username;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemFunctions {

	public void mergeData(Username username, UsernameRequest request) {
		username.setName(request.name());
		username.setPassword(request.password());
		username.setEmail(request.email());
	}
	
}
