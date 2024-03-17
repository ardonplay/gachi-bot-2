package io.github.ardonplay.gachibot2.services;


import io.github.ardonplay.gachibot2.config.BotConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MessageGenerationService {

    private final BotConfig botConfig;

    public SendMessage sendMessageWithReply(String text, Message message) {
        String chatId = message.getChatId().toString();
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        return sendMessage;
    }

    public SendMessage sendMessage(String text, Chat chat) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId());
        sendMessage.setText(text);
        return sendMessage;
    }

    public SendSticker sendSticker(String stickerPath, Message message) {
        String chatId = message.getChatId().toString();
        SendSticker sendSticker = new SendSticker(chatId, new InputFile(stickerPath));

        sendSticker.setReplyToMessageId(message.getMessageId());
        return sendSticker;
    }


    public SendAnimation sendAnimation(String path, Message message) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        InputFile inputFile = new InputFile(inputStream, path);

        SendAnimation sendAnimation = new SendAnimation();

        sendAnimation.setChatId(message.getChatId());
        sendAnimation.setReplyToMessageId(message.getMessageId());

        sendAnimation.setAnimation(inputFile);

        return sendAnimation;
    }

    private int stringsLength(List<String> text) {
        int length = 0;
        for (String word : text) {
            length += word.length();
        }
        return length;
    }

    private List<String> stringLimiter(List<String> text) {
        List<String> parts = new ArrayList<>();
        int length = stringsLength(text);
        while (length > botConfig.getMessageLength()) {
            final int mid = text.size() / 2;
            parts.add(text.subList(0, mid).toString());
            text = text.subList(mid, text.size());
            length = stringsLength(text);
        }
        parts.add(text.toString());
        return parts;
    }

}
