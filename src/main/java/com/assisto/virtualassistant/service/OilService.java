package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.model.Oil;
import com.assisto.virtualassistant.repository.OilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OilService {
    @Autowired
    private OilRepository oilRepository;

    public List<Oil> getAviableOils() {
        return oilRepository.findAll();
    }

    public Oil updateOilPrice(Long oilId, BigDecimal price) {
        Oil oil = oilRepository.findById(oilId).orElseThrow(() -> new RuntimeException("Oil not found"));
        oil.setPrice(price);
        return oilRepository.save(oil);
    }

    public List<Oil> getCompatibleOils(String vin) {
        return List.of(
                new Oil("Ulei 5W-30", "5W-30", new BigDecimal("100.00")),
                new Oil("Ulei 0W-20", "0W-20", new BigDecimal("120.00")),
                new Oil("Ulei 10W-40", "10W-40", new BigDecimal("80.00"))
        );
    }
}