package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Appointment;
import com.assisto.virtualassistant.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping({"/api/appointments"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.atTime(23, 59, 59);
        return ResponseEntity.ok(appointmentService.getAvailableSlots(start, end));
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(
            @RequestParam Long slotId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            @RequestParam String chassisNumber) {
        return ResponseEntity.ok(appointmentService.bookSlot(slotId, firstName, lastName, phoneNumber, chassisNumber));
    }
}
