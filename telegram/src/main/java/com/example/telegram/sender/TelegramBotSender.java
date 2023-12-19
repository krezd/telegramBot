package com.example.telegram.sender;

import com.example.telegram.property.TelegramBotProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotSender extends DefaultAbsSender {

    public TelegramBotSender(TelegramBotProperty property) {
        super(new DefaultBotOptions(), property.getToken());
    }

    public Message sendMessageBy(Long chatId, Integer messageId, String text) throws TelegramApiException {
        return execute(SendMessage.builder().chatId(chatId)
                .text(text)
                .build());
    }
}
