package com.example.telegram.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "telegram-bot.config")
public class TelegramBotProperty {
    private String name;
    private String token;
}
