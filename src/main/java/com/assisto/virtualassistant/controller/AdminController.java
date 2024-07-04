package com.assisto.virtualassistant.controller;

import com.assisto.virtualassistant.model.User;
import com.assisto.virtualassistant.repository.UserRepository;
import com.assisto.virtualassistant.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/admin"})
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;

    public AdminController() {
    }

    @PostMapping({"/addUser"})
    public void addUser(@RequestBody User user) {
        this.userRepository.save(user);
    }

    @PostMapping({"/sendMessage"})
    public void sendMessage(@RequestParam String to, @RequestParam String body) {
        this.messageService.sendMessage(to, body);
    }
}