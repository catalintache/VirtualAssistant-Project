package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

