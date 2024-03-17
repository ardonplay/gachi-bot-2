package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.config.BotConfig;
import io.github.ardonplay.gachibot2.services.filters.BadWordFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class BotService extends TelegramLongPollingCommandBot {

    private final BotConfig config;

    private final BadWordFilter badWordFilter;

    @Autowired
    public BotService(BotConfig config, List<BotCommand> botCommands, BadWordFilter badWordFilter) {
        super(config.getToken());
        this.config = config;
        this.badWordFilter = badWordFilter;
        botCommands.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            badWordFilter.check(message).ifPresent(this::execute);


        } else if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {

        }
    }

    private void execute(List<Object> message) {
        message.forEach(this::execute);
    }

    private void execute(Object responce) {
        try {
            log.info(responce.getClass().getSimpleName());
            switch (responce.getClass().getSimpleName()) {
                case "SendMessage" -> execute((SendMessage) responce);
                case "SendSticker" -> execute((SendSticker) responce);
                case "SendAnimation" -> execute((SendAnimation) responce);
                case "SendLocation" -> execute((SendLocation) responce);
                case "SendDocument" -> execute((SendDocument) responce);
                case "SendVideo" -> execute((SendVideo) responce);
                case "SendVoice" -> execute((SendVoice) responce);
                case "SendPhoto" -> execute((SendPhoto) responce);
                default -> throw new TelegramApiException("Bad Class Type!");
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


}
