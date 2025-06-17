package com.tom.first.username.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.username.common.SystemFunctions;
import com.tom.first.username.dto.NameRequest;
import com.tom.first.username.dto.UsernameRequest;
import com.tom.first.username.dto.UsernameResponse;
import com.tom.first.username.exception.AlreadyExistsException;
import com.tom.first.username.exception.NotFoundException;
import com.tom.first.username.mapper.UsernameMapper;
import com.tom.first.username.model.Username;
import com.tom.first.username.repository.UsernameRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsernameService {

	private final SystemFunctions system; 
	private final UsernameRepository repository;
	private final UsernameMapper mapper;

	public List<UsernameResponse> findAll() {
		List<Username> users = repository.findAll();
		if (users.isEmpty()) {
			throw new NotFoundException("No users found");
		}
		return users.stream().map(mapper::fromUsername).collect(Collectors.toList());
	}

	public UsernameResponse findById(Long id) {
		var user = repository.findById(id).map(mapper::fromUsername)
				.orElseThrow(() -> new NotFoundException(String.format("User with id: %s, was not found", id)));
		return user;
	}

	public UsernameResponse findByName(NameRequest request) {
		var user = repository.findOptionalByName(request.name()).map(mapper::fromUsername).orElseThrow(
				() -> new NotFoundException(String.format("User with id: %s, was not found", request.name())));
		return user;
	}

	@Transactional
	public Long createUsername(UsernameRequest request) {
		if (repository.existsByEmail(request.email())) {
			throw new AlreadyExistsException(
					String.format("User with same email already exists %s", request.email()));
		}
		var user = repository.save(mapper.toUsername(request));
		return user.getId();
	}

	@Transactional
	public UsernameResponse updateUsername(Long id, UsernameRequest request) {
		if (!repository.existsByName(request.name())) {
			throw new NotFoundException(String.format("User with email %s, was not found", request.name()));
		}
		var user = repository.findById(id).orElseThrow(
				() -> new NotFoundException(String.format("User with name %s, was not found", request.name())));
		system.mergeData(user, request);
		repository.save(user);
		return mapper.fromUsername(user);
	}

	@Transactional
	public void deleteUsernameById(Long userId) {
		if (!repository.existsById(userId)) {
			throw new NotFoundException(String.format("User with id: %s not found", userId));
		}
		repository.deleteById(userId);
	}

	@Transactional
	public void deleteUsernameByName(NameRequest request) {
		if (!repository.existsByName(request.name())) {
			throw new NotFoundException(String.format("User with name: %s not found", request.name()));
		}
		repository.deleteByName(request.name());
	}

}
