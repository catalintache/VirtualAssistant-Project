package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Offer;
import com.assisto.virtualassistant.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/offers"})
public class OfferController {
    @Autowired
    private OfferService offerService;

    public OfferController() {
    }

    @PostMapping
    public ResponseEntity<Offer> createOffer(
            @RequestParam String vin,
            @RequestParam String oilViscosity,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber) {
        return ResponseEntity.ok(this.offerService.createOffer(vin, oilViscosity, firstName, lastName, phoneNumber));
    }
}
