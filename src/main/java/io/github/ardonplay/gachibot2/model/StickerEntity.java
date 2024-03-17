package io.github.ardonplay.gachibot2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Entity(name = "TELEGRAM_STICKERS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StickerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    private String fileId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickerEntity stickerEntity = (StickerEntity) o;
        return Objects.equals(name, stickerEntity.name) && Objects.equals(fileId, stickerEntity.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fileId);
    }
}
