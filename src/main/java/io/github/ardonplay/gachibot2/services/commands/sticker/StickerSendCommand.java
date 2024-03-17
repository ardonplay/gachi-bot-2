package io.github.ardonplay.gachibot2.services.commands.sticker;

import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.StickerService;
import io.github.ardonplay.gachibot2.services.exceptions.StickerNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class StickerSendCommand extends BotCommand {
    private final StickerService stickerService;
    private final MessageGenerationService messageGenerationService;

    public StickerSendCommand(StickerService stickerService, MessageGenerationService messageGenerationService) {
        super("sticker", "Send sticker by name");
        this.stickerService = stickerService;
        this.messageGenerationService = messageGenerationService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage responseMessage = null;
        if (arguments.length == 1) {
            try {
                SendSticker sendSticker = messageGenerationService.sendSticker(stickerService.getSticker(arguments[0]).getFileId(), message);

                try {
                    absSender.execute(sendSticker);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }

            } catch (StickerNotExistException e) {
                responseMessage = messageGenerationService.sendMessageWithReply("Стикера " + arguments[0] + " не существует", message);
            }
        } else {
            responseMessage = messageGenerationService.sendMessageWithReply("Ну и что ты этим хотел сказать?", message);
        }
        if (responseMessage != null) {
            try {
                absSender.execute(responseMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {}
}
