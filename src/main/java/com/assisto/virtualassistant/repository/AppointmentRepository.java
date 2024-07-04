package com.assisto.virtualassistant.repository;

import com.assisto.virtualassistant.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByStartTimeBetweenAndIsAvailable(LocalDateTime start, LocalDateTime end, boolean isAvailable);
    List<Appointment> findByPhoneNumber(String phoneNumber);
}
