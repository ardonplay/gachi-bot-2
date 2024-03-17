package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.model.UserEntity;
import io.github.ardonplay.gachibot2.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public UserEntity findByid(Long id){
        return userRepository.resolveUser(id);
    }

    @Transactional
    public UserEntity updateUser(UserEntity user){
        return userRepository.save(user);
    }


}
