package io.github.ardonplay.gachibot2.repositories;

import io.github.ardonplay.gachibot2.model.StickerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StickerRepository extends CrudRepository<StickerEntity, Integer> {
    Optional<StickerEntity> findByFileId(String fileId);
    Optional<StickerEntity> findByName(String name);
}
