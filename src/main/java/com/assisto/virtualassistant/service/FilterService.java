package com.assisto.virtualassistant.service;


import com.assisto.virtualassistant.model.Filter;
import com.assisto.virtualassistant.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FilterService {
    @Autowired
    private FilterRepository filterRepository;

    public FilterService() {
    }

    public List<Filter> getAvailableFilters() {
        return this.filterRepository.findAll();
    }

    public Filter updateFilterPrice(Long filterId, BigDecimal price) {
        Filter filter = (Filter)this.filterRepository.findById(filterId).orElseThrow(() -> {
            return new RuntimeException("Filter not found");
        });
        filter.setPrice(price);
        return (Filter)this.filterRepository.save(filter);
    }

    public Filter addFilter(Filter filter) {
        return (Filter)this.filterRepository.save(filter);
    }
}

