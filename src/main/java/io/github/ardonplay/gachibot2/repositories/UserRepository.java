package io.github.ardonplay.gachibot2.repositories;


import io.github.ardonplay.gachibot2.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    default UserEntity resolveUser(Long id) {
        Optional<UserEntity> user = findById(id);
        return user.orElseGet(() -> save(UserEntity.builder().id(id).build()));
    }
}
