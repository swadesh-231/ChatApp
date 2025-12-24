package com.chatapp.controller;


import com.chatapp.entity.ChatMessages;
import com.chatapp.repository.ChatMessageRepository;
import com.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final ChatMessageRepository chatMessageRepository;


    @MessageMapping("/chat.add-user")
    @SendTo("/topic/public")
    public ChatMessages addUser(@Payload ChatMessages chatMessages, SimpMessageHeaderAccessor headerAccessor){
        if (userService.existUser(chatMessages.getSender())){
            headerAccessor.getSessionAttributes().put("username",chatMessages.getSender());
            userService.setUserStatusOnline(chatMessages.getSender(),true);
            chatMessages.setTimestamp(LocalDateTime.now());
            if (chatMessages.getMessage() == null){
                chatMessages.setMessage("");
            }
            return chatMessageRepository.save(chatMessages);
        }
        return null;
    }
    @MessageMapping("/chat.send-message")
    @SendTo("/topic/public")
    public ChatMessages sendMessage(@Payload ChatMessages chatMessages){
        return chatMessages;
    }
    @MessageMapping("/chat.send-private-message")
    public void sendPrivateMessage(@Payload ChatMessages chatMessages, SimpMessageHeaderAccessor headerAccessor){

    }
}
