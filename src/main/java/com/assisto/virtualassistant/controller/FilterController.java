package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Filter;
import com.assisto.virtualassistant.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping({"/api/filters"})
public class FilterController {
    @Autowired
    private FilterService filterService;

    public FilterController() {
    }

    @GetMapping
    public ResponseEntity<List<Filter>> getFilters() {
        return ResponseEntity.ok(this.filterService.getAvailableFilters());
    }

    @PostMapping({"/updatePrice"})
    public ResponseEntity<Filter> updateFilterPrice(@RequestParam Long filterId, @RequestParam BigDecimal price) {
        return ResponseEntity.ok(this.filterService.updateFilterPrice(filterId, price));
    }

    @PostMapping({"/add"})
    public ResponseEntity<Filter> addFilter(@RequestBody Filter filter) {
        return ResponseEntity.ok(this.filterService.addFilter(filter));
    }
}

