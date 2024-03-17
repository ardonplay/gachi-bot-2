package io.github.ardonplay.gachibot2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity(name = "TELEGRAM_USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    private Long id;

    private Integer hp;

    @OneToMany(mappedBy = "userEntity")
    private Set<BadWordStat> stats;
}
