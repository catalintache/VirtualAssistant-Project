package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Vehicle;
import com.assisto.virtualassistant.service.VehicleService;
import com.assisto.virtualassistant.service.VinDecoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VinDecoderService vinDecoderService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/decode")
    public ResponseEntity<Vehicle> decodeVin(@RequestParam String vin) {
        Vehicle vehicle = vinDecoderService.decodeVin(vin);
        vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok(vehicle);
    }
}