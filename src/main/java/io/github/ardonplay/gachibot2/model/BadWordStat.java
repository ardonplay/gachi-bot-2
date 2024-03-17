package io.github.ardonplay.gachibot2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity(name = "BAD_WORDS_STAT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(BadWordAnduserId.class)
public class BadWordStat {
    @Id
    private Integer badWordId;
    @Id
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userEntity", referencedColumnName = "id")
    private UserEntity userEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "badWord", referencedColumnName = "id")
    private BadWord badWord;

    private int count;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadWordStat that = (BadWordStat) o;
        return Objects.equals(badWordId, that.badWordId) && Objects.equals(userId, that.userId) && Objects.equals(userEntity, that.userEntity) && Objects.equals(badWord, that.badWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badWordId, userId, userEntity, badWord);
    }
}
