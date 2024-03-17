package io.github.ardonplay.gachibot2.services.commands.sticker;

import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.StickerService;
import io.github.ardonplay.gachibot2.services.exceptions.StickerAlreadyExistsException;
import io.github.ardonplay.gachibot2.services.exceptions.StickerNameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class RememberStickerCommand extends BotCommand {
    private final StickerService stickerService;
    private final MessageGenerationService messageGenerationService;
    public RememberStickerCommand(StickerService stickerService, MessageGenerationService messageGenerationService){
        super("remember", "Remember sticker to bot");
        this.stickerService = stickerService;
        this.messageGenerationService = messageGenerationService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage responseMessage;

        if(message.getReplyToMessage() != null && message.getReplyToMessage().hasSticker()){

            if(arguments.length == 1){
                Sticker sticker = message.getReplyToMessage().getSticker();
                try {
                    stickerService.saveSticker(arguments[0], sticker.getFileId());
                    responseMessage = messageGenerationService.sendMessageWithReply("Стикер с названием " + "\"" + arguments[0] + "\"" + "был успешно запомнен", message);
                }
                catch (StickerNameAlreadyExistsException e){
                    responseMessage = messageGenerationService.sendMessageWithReply("Такое имя уже занятно", message);
                }
                catch (StickerAlreadyExistsException e){
                    responseMessage = messageGenerationService.sendMessageWithReply("Я уже запоминал этот стикер", message);
                }


            }else {
                responseMessage = messageGenerationService.sendMessageWithReply("Я не могу запомнить то что не названо", message);
            }

        }else {
            responseMessage = messageGenerationService.sendMessageWithReply("Это не стикер, чел", message);
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
