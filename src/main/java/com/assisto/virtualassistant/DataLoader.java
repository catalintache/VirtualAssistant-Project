package com.assisto.virtualassistant;

import com.assisto.virtualassistant.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired


    private AppointmentService appointmentService;

    @Override
    public void run(String... args) throws Exception {
        LocalDateTime date = LocalDateTime.of(2024, 7, 4, 0, 0);
        List<LocalDateTime> startTimes = Arrays.asList(
                LocalDateTime.of(2024, 7, 4, 9, 0),
                LocalDateTime.of(2024, 7, 4, 10, 0),
                LocalDateTime.of(2024, 7, 4, 11, 0),
                LocalDateTime.of(2024, 7, 4, 14, 0),
                LocalDateTime.of(2024, 7, 4, 15, 0),
                LocalDateTime.of(2024, 7, 4, 16, 0)
        );

        appointmentService.addSlotsForDate(date, startTimes, 60);
    }
}