package com.example.telegram.listener;

import com.example.telegram.entity.AnecdotEntity;
import com.example.telegram.entity.UserEntity;
import com.example.telegram.property.TelegramBotProperty;
import com.example.telegram.repository.AnecdotRepository;
import com.example.telegram.repository.UserRepository;
import com.example.telegram.sender.TelegramBotSender;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.IMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotListenerLongPoll extends TelegramLongPollingBot {

    private final TelegramBotProperty telegramBotProperty;
    private final TelegramBotSender telegramBotSender;
    private final AnecdotRepository anecdotRepository;
    private final UserRepository userRepository;
    private List<AnecdotEntity> anecdotList;

    public BotListenerLongPoll(TelegramBotProperty property, TelegramBotSender sender, AnecdotRepository anecdotRepository, UserRepository userRepository) {
        super(property.getToken());
        telegramBotProperty = property;
        telegramBotSender = sender;
        this.anecdotRepository = anecdotRepository;
        anecdotList = anecdotRepository.findAll();
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getUserName();
            switch (messageText) {
                case "/start":
                    createAnecdoteKeyboard(chatId,"Привет, " + name + ", чтобы получить бот напиши /getAnecdot или нажми на кнопку 'Получить анекдот'");
                    break;
                case "/getAnecdot":
                    getAnecdot(update);
                    break;
                case "Получить анекдот":
                    getAnecdot(update);
                    break;
            }
            log.info(String.format("ChatId : %s", update.getMessage().getChatId().toString()));
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
    }
    

    private void getAnecdot(Update update) throws TelegramApiException {
        String name = update.getMessage().getChat().getUserName();
        UserEntity user = userRepository.findByUsername(name).orElse(null);
        int anecdotId = 0;
        if (user != null) {
            anecdotId = user.getLastAnecdotId();
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(name);
            newUser.setLastAnecdotId(0);
            userRepository.save(newUser);
            user = newUser;
        }

        if (anecdotId >= 0 && anecdotId < anecdotList.size()) {
            String result = anecdotList.get(anecdotId).getAnecdot();
            telegramBotSender.sendMessageBy(update.getMessage().getChatId(), update.getMessage().getMessageId(), result);
            anecdotId++;
            user.setLastAnecdotId(anecdotId);
            userRepository.save(user);
        } else {
            user.setLastAnecdotId(0);
            userRepository.save(user);
            telegramBotSender.sendMessageBy(update.getMessage().getChatId(), update.getMessage().getMessageId(), "Новые анекдоты закончились");
        }
    }

    private void createAnecdoteKeyboard(Long chatId,String text) {
        SendMessage  message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Получить анекдот");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Error create button");
        }
    }
    @Override
    public String getBotUsername() {
        return telegramBotProperty.getName();
    }
}
