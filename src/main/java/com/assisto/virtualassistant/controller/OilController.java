package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Oil;
import com.assisto.virtualassistant.service.OilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping({"/api/oils"})
public class OilController {
    @Autowired
    private OilService oilService;

    public OilController() {
    }

    @GetMapping
    public ResponseEntity<List<Oil>> getOils() {
        return ResponseEntity.ok(this.oilService.getAviableOils());
    }

    @PostMapping({"/updatePrice"})
    public ResponseEntity<Oil> updateOilPrice(@RequestParam Long oilId, @RequestParam BigDecimal price) {
        return ResponseEntity.ok(this.oilService.updateOilPrice(oilId, price));
    }
}
