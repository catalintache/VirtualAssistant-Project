package com.assisto.virtualassistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private ChatGPTService chatGPTService;

    public MessageService() {
    }

    public void sendMessage(String to, String body) {
        System.out.println("[-LOG-] === Sending message to: " + to);
        System.out.println("Message: " + body);
        System.out.println();
    }

    public void receiveMessage(String from, String body) {
        String response = this.chatGPTService.getChatGPTResponse(body);
        System.out.println("[-LOG-] === Received message from: " + from);
        System.out.println("Message: " + body);
        System.out.println("Response: " + response);
        System.out.println();
        this.sendMessage(from, response);
    }
}

