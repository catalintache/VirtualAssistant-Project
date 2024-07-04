package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByVin(String vin);
}
