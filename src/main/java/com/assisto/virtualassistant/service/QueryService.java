package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.model.Query;
import com.assisto.virtualassistant.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QueryService {
    @Autowired
    private QueryRepository queryRepository;

    public QueryService() {
    }

    public void logQuery(String vin, String query) {
        Query newQuery = new Query();
        newQuery.setVin(vin);
        newQuery.setQuery(query);
        newQuery.setTimestamp(LocalDateTime.now());
        this.queryRepository.save(newQuery);
    }
}
