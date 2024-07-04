package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Vehicle;
import com.assisto.virtualassistant.service.VinDecoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api"})
public class MainController {
    @Autowired
    private VinDecoderService vinDecoderService;

    public MainController() {
    }

    @GetMapping({"/vin/decode"})
    public ResponseEntity<Vehicle> decodeVin(@RequestParam String vin) {
        Vehicle vehicle = this.vinDecoderService.decodeVin(vin);
        return ResponseEntity.ok(vehicle);
    }
}

