package io.github.ardonplay.gachibot2.repositories;

import io.github.ardonplay.gachibot2.model.BadWord;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface BadWordRepository extends CrudRepository<BadWord, Long> {
    Optional<BadWord> findByWord(String word);

    void deleteByWord(String word);
}
