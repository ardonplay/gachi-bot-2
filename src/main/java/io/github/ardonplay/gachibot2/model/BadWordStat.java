package io.github.ardonplay.gachibot2.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity(name = "BAD_WORDS_STAT")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BadWordStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ENTITY", referencedColumnName = "id")
    private UserEntity userEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BAD_WORD", referencedColumnName = "id")
    private BadWord badWord;

    private int count;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadWordStat that = (BadWordStat) o;
        return Objects.equals(userEntity, that.userEntity) && Objects.equals(badWord, that.badWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEntity, badWord);
    }
}
