package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.model.Filter;
import com.assisto.virtualassistant.model.Offer;
import com.assisto.virtualassistant.model.Oil;
import com.assisto.virtualassistant.repository.FilterRepository;
import com.assisto.virtualassistant.repository.OfferRepository;
import com.assisto.virtualassistant.repository.OilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OfferService {
    @Autowired
    private OilRepository oilRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private OfferRepository offerRepository;

    public OfferService() {
    }

    public Offer createOffer(String vin, String oilViscosity, String firstName, String lastName, String phoneNumber) {
        List<Oil> oils = this.oilRepository.findByViscosity(oilViscosity);
        if (oils.isEmpty()) {
            throw new RuntimeException("No oils found with viscosity: " + oilViscosity);
        } else {
            Oil selectedOil = oils.get(0);
            List<Filter> filters = this.filterRepository.findAll();
            BigDecimal totalPrice = selectedOil.getPrice();
            String filterBrand = null;
            if (!filters.isEmpty()) {
                Filter selectedFilter = filters.get(0);
                totalPrice = totalPrice.add(selectedFilter.getPrice());
                filterBrand = selectedFilter.getBrand();
            }

            Offer offer = new Offer();
            offer.setVin(vin);
            offer.setOilType(selectedOil.getType());
            offer.setFilterBrand(filterBrand);
            offer.setTotalPrice(totalPrice);
            offer.setFirstName(firstName);
            offer.setLastName(lastName);
            offer.setPhoneNumber(phoneNumber);
            offer.setOfferNumber(generateOfferNumber());

            offerRepository.save(offer);
            return offer;
        }
    }

    private String generateOfferNumber() {
        return "OFFER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}


