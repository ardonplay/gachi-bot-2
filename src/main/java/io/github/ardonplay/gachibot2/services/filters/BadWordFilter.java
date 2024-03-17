package io.github.ardonplay.gachibot2.services.filters;

import io.github.ardonplay.gachibot2.model.BadWord;
import io.github.ardonplay.gachibot2.model.BadWordStat;
import io.github.ardonplay.gachibot2.services.BadWordService;
import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadWordFilter {

    private final BadWordService badWordService;

    private final UserService userService;

    private final MessageGenerationService messageGenerationService;

    private List<Object> response;

    public BadWordFilter check(Message message) {
        int count = 0;

        List<String> words = List.of(message.getText().split(" "));
        List<BadWord> badWords = badWordService.getBadWords();
        List<BadWordStat> userBadWordStats = new ArrayList<>();
        for (BadWord badWord : badWords) {
            for (String word : words) {
                if (word.contains(badWord.getWord())) {
                    count++;
                }
            }
        }

        if (count > 0) {
            response = counterSwitcher(count, message);
        }
        return this;
    }

    public void ifPresent(Consumer<? super List<Object>> action) {
        if (response != null) {
            action.accept(response);
        }
    }



    private List<Object> counterSwitcher(int count, Message message) {
        switch (count) {
            case 0, 1, 2 -> {
                return List.of(messageGenerationService.sendMessageWithReply("🤡", message));
            }
            case 3 -> {
                return List.of(messageGenerationService.sendSticker("CAACAgIAAxkBAAEIjSBkNlgpIQuL_ga8xvnrRYyTU3-NAwACAhgAAnLcwEt56Ty4vsWYDC8E", message),
                        messageGenerationService.sendMessage("За такие слова я тебя сейчас в бан кину", message));
            }
            default -> {
                return List.of(restrictUser(message.getChatId().toString(), message.getFrom().getId()),
                        messageGenerationService.sendMessageWithReply("Ну ты дописался, посиди в бане минутку", message));
            }
        }
    }

    RestrictChatMember restrictUser(String chatId, long userId) {
        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(false);
        chatPermissions.setCanSendPolls(false);
        chatPermissions.setCanSendOtherMessages(false);
        chatPermissions.setCanAddWebPagePreviews(false);

        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setChatId(chatId);
        restrictChatMember.setUserId(userId);
        restrictChatMember.forTimePeriodDuration(Duration.ofMinutes(1));
        restrictChatMember.setPermissions(chatPermissions);

        return restrictChatMember;

    }

}
