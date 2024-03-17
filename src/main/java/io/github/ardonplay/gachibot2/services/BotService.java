package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.config.BotConfig;
import io.github.ardonplay.gachibot2.services.filters.BadWordFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class BotService extends TelegramLongPollingCommandBot {

    private final BotConfig config;

    private final MessageGenerationService messageGenerationService;

    private final BadWordFilter badWordFilter;

    @Autowired
    public BotService(BotConfig config, List<BotCommand> botCommands, MessageGenerationService messageGenerationService, BadWordFilter badWordFilter) {
        super(config.getToken());
        this.config = config;
        this.messageGenerationService = messageGenerationService;
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
            badWordFilter.check(message).ifNotPassed(this::execute);
        } else if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
            Message message = update.getMessage();

            try {
                Long botID = getMeAsync().get().getId();
                for (var user : message.getNewChatMembers()) {
                    if (user.getId().equals(botID)) {
                        this.execute(messageGenerationService.sendMessage("Ну привет мои маленькие slaves," +
                                " зовите меня своим dungeon masterом, " +
                                "я вам не дам повода материться," +
                                " а если вы будете это делать - придется " +
                                "сделать fisting ass...", message.getChat()));
                    }
                }
            } catch (InterruptedException | ExecutionException | TelegramApiException e) {
                log.error(e.getMessage());
            }
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
                case "RestrictChatMember" -> execute((RestrictChatMember) responce);
                default -> throw new TelegramApiException("Bad Class Type!");
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


}
