package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.model.StickerEntity;
import io.github.ardonplay.gachibot2.repositories.StickerRepository;
import io.github.ardonplay.gachibot2.services.exceptions.StickerAlreadyExistsException;
import io.github.ardonplay.gachibot2.services.exceptions.StickerNameAlreadyExistsException;
import io.github.ardonplay.gachibot2.services.exceptions.StickerNotExistException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;

    @Transactional
    @CacheEvict(value = "stickers", allEntries = true)
    public StickerEntity saveSticker(String name, String fileId) throws StickerAlreadyExistsException, StickerNameAlreadyExistsException {
        if (stickerRepository.findByName(name).isPresent()) {
            throw new StickerNameAlreadyExistsException();
        }
        if (stickerRepository.findByFileId(fileId).isPresent()) {
            throw new StickerAlreadyExistsException();
        }

        StickerEntity entity = StickerEntity.builder().fileId(fileId).name(name).build();

        return stickerRepository.save(entity);
    }

    public StickerEntity getSticker(String name) throws StickerNotExistException{
        Optional<StickerEntity> sticker = stickerRepository.findByName(name);
        if (sticker.isPresent()) {
            return sticker.get();
        }else {
            throw new StickerNotExistException();
        }
    }

    @Cacheable("stickers")
    public List<String> getStickerNames() {
        return StreamSupport
                .stream(stickerRepository.findAll().spliterator(), false).map(StickerEntity::getName).toList();
    }
}
