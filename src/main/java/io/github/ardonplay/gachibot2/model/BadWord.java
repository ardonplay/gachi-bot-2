package io.github.ardonplay.gachibot2.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity(name = "BAD_WORDS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BadWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    private Integer level;

    @OneToMany(mappedBy = "badWord", cascade = CascadeType.ALL)
    private List<BadWordStat> stats;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadWord badWord = (BadWord) o;
        return Objects.equals(id, badWord.id) && Objects.equals(word, badWord.word) && Objects.equals(level, badWord.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, level);
    }
}
