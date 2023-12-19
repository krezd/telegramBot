package com.example.telegram.configuration;

import com.example.telegram.listener.BotListenerLongPoll;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotListenerLongPollConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(BotListenerLongPoll botListenerLongPoll) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(botListenerLongPoll);
        return api;
    }
}
