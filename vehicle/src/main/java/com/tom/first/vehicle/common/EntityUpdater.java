package com.tom.first.vehicle.common;

import org.springframework.stereotype.Service;

import com.tom.first.vehicle.model.Vehicle;
import com.tom.first.vehicle.request.VehicleUpdate;

@Service
public class EntityUpdater {

	public void mergeVehicle(Vehicle vehicle, VehicleUpdate request) {
		vehicle.setBrand(request.brand());
		vehicle.setModel(request.model());
		vehicle.setColor(request.color());
		vehicle.setPlate(request.licensePlate());
		vehicle.setType(request.type());
	}
	
}
