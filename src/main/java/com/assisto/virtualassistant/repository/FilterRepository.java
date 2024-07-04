package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}