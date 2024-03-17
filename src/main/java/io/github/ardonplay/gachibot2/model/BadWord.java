package io.github.ardonplay.gachibot2.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "BAD_WORDS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BadWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    @OneToMany(mappedBy = "badWord")
    private List<BadWordStat> stats;
}
