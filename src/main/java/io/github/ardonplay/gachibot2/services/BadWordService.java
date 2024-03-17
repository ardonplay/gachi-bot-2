package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.model.BadWord;
import io.github.ardonplay.gachibot2.repositories.BadWordRepository;
import io.github.ardonplay.gachibot2.services.exceptions.WordAlreadyExistsException;
import io.github.ardonplay.gachibot2.services.exceptions.WordNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadWordService {

    @Autowired
    private BadWordService self;

    private final BadWordRepository badWordRepository;

    @Transactional
    @CacheEvict(value = "bad_words", allEntries = true)
    public void addWord(String word, Integer level) throws WordAlreadyExistsException {

        List<BadWord> badWords = self.getBadWords();
        if (badWords.stream().anyMatch(badWord -> badWord.getWord().equals(word)))
            throw new WordAlreadyExistsException();

        Optional<BadWord> badWord = badWordRepository.findByWord(word);

        if (badWord.isPresent()) {
            throw new WordAlreadyExistsException();
        } else {
            badWordRepository.save(BadWord.builder().level(level).word(word).build());
        }
    }

    @Transactional
    @CacheEvict(value = "bad_words", allEntries = true)
    public void removeWord(String word) throws WordNotFoundException {
        List<BadWord> badWords = self.getBadWords();
        if (badWords.stream().noneMatch(badWord -> badWord.getWord().equals(word))) {
            throw new WordNotFoundException();
        } else {
            badWordRepository.deleteByWord(word);
        }

    }

    @Cacheable("bad_words")
    public List<BadWord> getBadWords() {

        return StreamSupport
                .stream(badWordRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

}
