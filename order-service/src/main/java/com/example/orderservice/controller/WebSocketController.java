package com.example.orderservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class WebSocketController {

    @MessageMapping("/message")  // Nhận yêu cầu từ /app/message
    @SendTo("/topic/orders")  // Gửi tin nhắn tới client lắng nghe tại /topic/messages
    public String handleMessage(String message) {
        return message; // Trả về phản hồi
    }
}
