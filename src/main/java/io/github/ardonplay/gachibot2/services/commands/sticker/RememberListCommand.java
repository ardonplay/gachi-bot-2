package io.github.ardonplay.gachibot2.services.commands.sticker;

import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.StickerService;
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
public class RememberListCommand extends BotCommand {

    private final StickerService stickerService;
    private final MessageGenerationService messageGenerationService;

    public RememberListCommand(StickerService stickerService, MessageGenerationService messageGenerationService) {
        super("stickers", "Returns list of  stickers thats bot remember");
        this.stickerService = stickerService;
        this.messageGenerationService = messageGenerationService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage responseMessage = messageGenerationService.sendMessage("Имена стикеров: " + stickerService.getStickerNames().toString(), chat);
        try {
            absSender.execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
