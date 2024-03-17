package io.github.ardonplay.gachibot2.services.filters;

import io.github.ardonplay.gachibot2.model.BadWord;
import io.github.ardonplay.gachibot2.model.BadWordStat;
import io.github.ardonplay.gachibot2.model.UserEntity;
import io.github.ardonplay.gachibot2.services.BadWordService;
import io.github.ardonplay.gachibot2.services.MessageGenerationService;
import io.github.ardonplay.gachibot2.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadWordFilter {

    private final BadWordService badWordService;

    private final UserService userService;

    private final MessageGenerationService messageGenerationService;

    private List<Object> response;


    @Transactional
    public BadWordFilter check(Message message) {
        int level = 0;

        List<String> words = List.of(message.getText().split(" "));

        log.info("Message: {}", words);
        List<BadWord> badWords = badWordService.getBadWords();
        log.info("BadWords: {}", badWords);
        Map<BadWord, Integer> userBadWordStats = new HashMap<>();
        for (BadWord badWord : badWords) {
            for (String word : words) {
                if (word.contains(badWord.getWord())) {
                    if (userBadWordStats.containsKey(badWord)) {
                        userBadWordStats.put(badWord, userBadWordStats.get(badWord) + 1);
                    } else {
                        userBadWordStats.put(badWord, 1);
                    }
                    level = Math.max(level, badWord.getLevel());
                }
            }
        }

        if (level > 0) {
            log.info("Count: {}", level);
            UserEntity user = userService.findByid(message.getFrom().getId());

            Set<BadWordStat> newStat = userBadWordStats.entrySet().stream().map(stat -> BadWordStat.builder()
                    .badWord(stat.getKey())
                    .userEntity(user)
                    .count(stat.getValue()).build()).collect(Collectors.toSet());

            Set<BadWordStat> oldstat = user.getStats().stream().filter(newStat::contains).peek(e -> e.setCount(
                            newStat.stream()
                                    .filter(b -> b.equals(e)).findFirst()
                                    .orElse(new BadWordStat()).getCount() + e.getCount()))
                    .collect(Collectors.toSet());

            newStat.addAll(oldstat);
            user.getStats().addAll(newStat);
            userService.updateUser(user);

            response = levelResponses(level, message);
        }
        return this;
    }

    public void ifNotPassed(Consumer<? super List<Object>> action) {
        if (response != null) {
            action.accept(response);
            response = null;
        }
    }


    private List<Object> levelResponses(int level, Message message) {
        switch (level) {
            case 0 -> {
                return List.of(messageGenerationService.sendMessageWithReply("🤡", message));
            }
            case 1 -> {
                return List.of(messageGenerationService.sendSticker("CAACAgIAAxkBAAEIjSBkNlgpIQuL_ga8xvnrRYyTU3-NAwACAhgAAnLcwEt56Ty4vsWYDC8E", message),
                        messageGenerationService.sendMessage("За такие слова я тебя сейчас в бан кину", message.getChat()));
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
