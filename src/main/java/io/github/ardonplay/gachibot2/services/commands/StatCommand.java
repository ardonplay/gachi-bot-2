package io.github.ardonplay.gachibot2.services.commands;

import io.github.ardonplay.gachibot2.model.BadWordStat;
import io.github.ardonplay.gachibot2.model.UserEntity;
import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class StatCommand extends BotCommand {

    private final UserService userService;

    private final MessageGenerationService messageGenerationService;

    public StatCommand(UserService userService, MessageGenerationService messageGenerationService) {
        super("stat", "Return stat of bad words");
        this.userService = userService;
        this.messageGenerationService = messageGenerationService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        UserEntity userEntity = userService.findByid(message.getFrom().getId());

        SendMessage responseMessage;

        if (userEntity.getStats().isEmpty()) {
           responseMessage = messageGenerationService.sendMessageWithReply("Ого, а вы даже не матерились, пока что)", message);
        }else {
            responseMessage = messageGenerationService.sendMessageWithReply(userEntity.getStats().stream().map(s -> "{" + s.getBadWord().getWord() + ":" + s.getCount() + "}").toList().toString(), message);
        }
        try {
            absSender.execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {}
}
