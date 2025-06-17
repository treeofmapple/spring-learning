package com.tom.first.establishment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tom.first.establishment.common.EntityUpdater;
import com.tom.first.establishment.config.ServiceLogger;
import com.tom.first.establishment.dto.EstablishmentRequest;
import com.tom.first.establishment.dto.EstablishmentResponse;
import com.tom.first.establishment.dto.EstablishmentUpdate;
import com.tom.first.establishment.exception.AlreadyExistsException;
import com.tom.first.establishment.exception.NotFoundException;
import com.tom.first.establishment.mapper.EstablishmentMapper;
import com.tom.first.establishment.model.Establishment;
import com.tom.first.establishment.repository.EstablishmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

	private final EstablishmentRepository repository;
	private final EstablishmentMapper mapper;
	private final EntityUpdater system;

	public List<EstablishmentResponse> findAll() {
		ServiceLogger.info("Requesting all establishment data");
		List<Establishment> establishment = repository.findAll();
		if (establishment.isEmpty()) {
			ServiceLogger.warn("No establishments found in the database.");
			throw new NotFoundException("No establishments were found.");
		}
		ServiceLogger.info("Found " + establishment.size() + " establishments");
		return establishment.stream().map(mapper::fromEstablishment).collect(Collectors.toList());
	}

	public EstablishmentResponse findByName(String name) {
		ServiceLogger.info("Searching for establishment by name: " + name);
		return repository.findByName(name)
				.map(mapper::fromEstablishment)
				.orElseThrow(() -> {
					ServiceLogger.warn("Establishment with name '" + name + "' not found");
					return new NotFoundException(String.format("Establishment with name '%s' was not found.", name));
				});
	}

	@Transactional
	public EstablishmentResponse createEstablishment(EstablishmentRequest request) {
		ServiceLogger.info("Creating new establishment with name: '" + request.name());

		if (repository.existsByNameAndCnpj(request.name(), request.cnpj())) {
			ServiceLogger.warn("Attempted to create establishment that already exists with name: '" + request.name());
			throw new AlreadyExistsException(
					String.format("An establishment with name '%s' and CNPJ '%s' already exists.", request.name(), request.cnpj())
			);
		}

		var establishment = mapper.toEstablishment(request);
		repository.save(establishment);
		ServiceLogger.info("Successfully created establishment with name: '" + establishment.getName() + "'");
		return mapper.fromEstablishment(establishment);
	}

	@Transactional
	public EstablishmentResponse updateEstablishment(String name, EstablishmentUpdate request) {
		ServiceLogger.info("Updating establishment with name: '" + name + "'");

		var establishment = repository.findByName(name)
				.orElseThrow(() -> {
					ServiceLogger.warn("Failed to update - establishment with name '" + name + "' not found");
					return new NotFoundException(String.format("Establishment with name '%s' was not found.", name));
				});

		system.mergeEstablishment(establishment, request);
		repository.save(establishment);
		ServiceLogger.info("Successfully updated establishment: '" + name + "'");
		return mapper.fromEstablishment(establishment);
	}

	@Transactional
	public void deleteEstablishment(String name) {
		ServiceLogger.info("Attempting to delete establishment with name: '" + name + "'");

		if (!repository.existsByName(name)) {
			ServiceLogger.warn("Deletion failed - establishment with name '" + name + "' does not exist");
			throw new NotFoundException(String.format("Establishment with name '%s' does not exist.", name));
		}
		repository.deleteByName(name);
		ServiceLogger.info("Successfully deleted establishment with name: '" + name + "'");
	}
}
