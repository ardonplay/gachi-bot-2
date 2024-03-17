package io.github.ardonplay.gachibot2.services.commands;

import io.github.ardonplay.gachibot2.services.BadWordService;
import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.exceptions.WordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class DeleteBadWordCommand extends BotCommand {
    private final MessageGenerationService messageGenerationService;

    private final BadWordService badWordService;
    public DeleteBadWordCommand(MessageGenerationService messageGenerationService, BadWordService badWordService) {
        super("delete_bad_word", "Deletes bad word to bot");
        this.messageGenerationService = messageGenerationService;
        this.badWordService = badWordService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage responseMessage;

        if (arguments.length == 0) {
            responseMessage = messageGenerationService.sendMessage("И что ты этим хотел сказать?", chat);
        } else {
            if (arguments.length > 1) {
                responseMessage = messageGenerationService.sendMessage("Многа букав", chat);
            } else {
                String word = arguments[0];
                try {
                    badWordService.removeWord(word);
                    responseMessage = messageGenerationService.sendMessage("Слово " + "\"" + word + "\"" + " удалено успешно", chat);
                }catch (WordNotFoundException e){
                    responseMessage = messageGenerationService.sendMessage("Слово " + "\"" + word + "\"" + " не существует!", chat);
                }
            }

        }

        try {
            absSender.execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}