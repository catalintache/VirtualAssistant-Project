package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query, Long> {
}