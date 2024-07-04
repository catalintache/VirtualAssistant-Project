package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Oil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OilRepository extends JpaRepository<Oil, Long> {
    List<Oil> findByViscosity(String viscosity);
}