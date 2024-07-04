package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.model.Vehicle;
import com.assisto.virtualassistant.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle findVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin);
    }
}
