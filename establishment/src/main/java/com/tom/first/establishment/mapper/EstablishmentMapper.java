package com.tom.first.establishment.mapper;

import org.springframework.stereotype.Service;

import com.tom.first.establishment.dto.EstablishmentRequest;
import com.tom.first.establishment.dto.EstablishmentResponse;
import com.tom.first.establishment.model.Establishment;

@Service
public class EstablishmentMapper {

	public Establishment toEstablishment(EstablishmentRequest request) {
		if(request == null) {
			return null;
		}
		
		return Establishment.builder()
				.name(request.name())
				.cnpj(request.cnpj())
				.address(request.address())
				.telephone(request.phone())
				.vacanciesMotorcycles(request.motorcycleSpotCount())
				.vacanciesCars(request.carSpotCount())
				.build();
		
	}
	
	public EstablishmentResponse fromEstablishment(Establishment request) {
		return new EstablishmentResponse(
				request.getName(),
				request.getCnpj(),
				request.getAddress(),
				request.getTelephone(),
				request.getVacanciesMotorcycles(),
				request.getVacanciesCars()
				);
	}
	
}
