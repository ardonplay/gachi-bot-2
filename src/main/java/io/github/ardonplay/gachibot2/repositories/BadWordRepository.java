package io.github.ardonplay.gachibot2.repositories;

import io.github.ardonplay.gachibot2.model.BadWord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface BadWordRepository extends JpaRepository<BadWord, Long> {
    Optional<BadWord> findByWord(String word);

    void deleteByWord(String word);

    @Query("SELECT c FROM BAD_WORDS c ORDER BY LENGTH(c.word) DESC")
    List<BadWord> findAllByOrderByWordAsc();
}
