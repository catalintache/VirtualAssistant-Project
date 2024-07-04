package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}