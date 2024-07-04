package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.Oil;
import com.assisto.virtualassistant.service.AppointmentService;
import com.assisto.virtualassistant.service.ChatGPTService;
import com.assisto.virtualassistant.service.OilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/chatgpt"})
public class ChatGPTController {
    @Autowired
    private ChatGPTService chatGPTService;
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private OilService oilService;

    public ChatGPTController() {
    }

    @GetMapping({"/menu"})
    public ResponseEntity<String> getMenu() {
        String menu = "Bun venit! Alegeți una dintre următoarele opțiuni:\n1. Programare\n2. Verificare VIN\n3. Verificare ulei compatibil\n4. Ofertă";
        return ResponseEntity.ok(menu);
    }

    @PostMapping({"/send"})
    public ResponseEntity<String> sendPrompt(@RequestParam String prompt) {
        String response = this.chatGPTService.getChatGPTResponse(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/appointment/check"})
    public ResponseEntity<String> checkAppointmentSlot(@RequestParam Long slotId) {
        boolean isAvailable = this.appointmentService.checkSlotAvailability(slotId);
        return ResponseEntity.ok(isAvailable ? "Slot is available" : "Slot is not available");
    }

    @GetMapping({"/oil/compatible"})
    public ResponseEntity<List<Oil>> getCompatibleOils1(@RequestParam String vin) {
        List<Oil> oils = oilService.getCompatibleOils(vin);
        return ResponseEntity.ok(oils);
    }
}

