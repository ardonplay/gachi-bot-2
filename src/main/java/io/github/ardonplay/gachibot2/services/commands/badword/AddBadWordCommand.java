package io.github.ardonplay.gachibot2.services.commands.badword;

import io.github.ardonplay.gachibot2.services.BadWordService;
import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.exceptions.WordAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;


@Slf4j
@Component
public class AddBadWordCommand extends BotCommand {

    private final MessageGenerationService messageGenerationService;

    private final BadWordService badWordService;

    public AddBadWordCommand(MessageGenerationService messageGenerationService, BadWordService badWordService) {
        super("add_bad_word", "Adds bad word to bot");
        this.messageGenerationService = messageGenerationService;
        this.badWordService = badWordService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage responseMessage = responseProcessor(chat, arguments);
        try {
            absSender.execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private SendMessage responseProcessor(Chat chat, String[] arguments) {
        if (arguments.length < 2) {
            return messageGenerationService.sendMessage("И что ты этим хотел сказать?", chat);
        } else {
            if (arguments.length > 2) {
                return messageGenerationService.sendMessage("Многа букав", chat);
            } else {
                String word = arguments[0];
                int level;
                if(word.length() < 4 || word.length() > 255){
                    return messageGenerationService.sendMessage("Господа, не сходите с ума...", chat);
                }
                try {
                    level = Integer.parseInt(arguments[1]);

                    if(level < 0){
                        return messageGenerationService.sendMessage("Это типа как?", chat);
                    }
                } catch (NumberFormatException e) {
                    return messageGenerationService.sendMessage("По вашему " + arguments[1] + " это цифра????", chat);
                }
                try {
                    badWordService.addWord(word, level);
                    return messageGenerationService.sendMessage("Слово " + "\"" + word + "\"" + " добавлено успешно", chat);
                } catch (WordAlreadyExistsException e) {
                    return messageGenerationService.sendMessage("Слово " + "\"" + word + "\"" + " уже существует!", chat);
                }
            }

        }
    }
}