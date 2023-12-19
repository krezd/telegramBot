package com.example.telegram.handler;

import com.example.telegram.sender.TelegramBotSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@RequiredArgsConstructor
public class BotHandler {

    private final TelegramBotSender telegramBotSender;

    public Message sendMessage(Long chatId) {
        try {
            return telegramBotSender.sendMessageBy(chatId, null, "Hi!");
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
            return null;
        }

    }
}
