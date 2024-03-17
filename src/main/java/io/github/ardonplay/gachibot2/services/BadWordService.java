package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.model.BadWord;
import io.github.ardonplay.gachibot2.repositories.BadWordRepository;
import io.github.ardonplay.gachibot2.services.exceptions.WordAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class BadWordService {
    private final BadWordRepository badWordRepository;

    private List<BadWord> badWords;

    public void addWord(String word) throws WordAlreadyExistsException {

        if (badWords.stream().anyMatch(badWord -> badWord.getWord().equals(word)))
            throw new WordAlreadyExistsException();

        Optional<BadWord> badWord = badWordRepository.findByWord(word);

        if (badWord.isPresent()) {
            throw new WordAlreadyExistsException();
        } else {
            badWordRepository.save(BadWord.builder().word(word).build());
        }
    }

    public void removeWord(String word) {
        badWordRepository.deleteByWord(word);
    }

    @Cacheable("bad_words")
    public List<BadWord> getBadWords() {

        this.badWords = StreamSupport
                .stream(badWordRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return badWords;
    }

}
