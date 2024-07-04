package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.model.Appointment;
import com.assisto.virtualassistant.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAvailableSlots(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findAllByStartTimeBetweenAndIsAvailable(start, end, true);
    }

    public Appointment bookSlot(Long slotId, String firstName, String lastName, String phoneNumber, String chassisNumber) {
        Appointment appointment = appointmentRepository.findById(slotId).orElseThrow(() -> new RuntimeException("Slot not found"));
        if (!appointment.isAvailable()) {
            throw new RuntimeException("Slot not available");
        } else {
            appointment.setAvailable(false);
            appointment.setFirstName(firstName);
            appointment.setLastName(lastName);
            appointment.setPhoneNumber(phoneNumber);
            appointment.setChassisNumber(chassisNumber);
            return appointmentRepository.save(appointment);
        }
    }

    public void addSlotsForDate(LocalDateTime date, List<LocalDateTime> startTimes, int slotDurationMinutes) {
        for (LocalDateTime startTime : startTimes) {
            Appointment slot = new Appointment();
            slot.setStartTime(startTime);
            slot.setEndTime(startTime.plusMinutes(slotDurationMinutes));
            slot.setAvailable(true);
            appointmentRepository.save(slot);
        }
    }

    public List<Appointment> getAppointmentsByPhoneNumber(String phoneNumber) {
        String normalizedPhoneNumber = phoneNumber.replaceAll("\\s+", "");
        return appointmentRepository.findByPhoneNumber(normalizedPhoneNumber);
    }

    public boolean checkSlotAvailability(Long slotId) {
        return appointmentRepository.findById(slotId)
                .map(Appointment::isAvailable)
                .orElse(false);
    }

}