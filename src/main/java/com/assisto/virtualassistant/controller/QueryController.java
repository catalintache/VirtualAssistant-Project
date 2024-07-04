package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/queries"})
public class QueryController {
    @Autowired
    private QueryService queryService;

    public QueryController() {
    }

    @PostMapping
    public ResponseEntity<Void> logQuery(@RequestParam String vin, @RequestParam String query) {
        this.queryService.logQuery(vin, query);
        return ResponseEntity.ok().build();
    }
}
