package com.example.telegram.controller;

import com.example.telegram.handler.BotHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Message;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotController {

    private final BotHandler botHandler;

    @PostMapping("/sendMessage")
    public Message sendMessage(@RequestParam Long chatId) {
        return botHandler.sendMessage(chatId);
    }
}
