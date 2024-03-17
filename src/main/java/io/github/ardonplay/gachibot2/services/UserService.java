package io.github.ardonplay.gachibot2.services;

import io.github.ardonplay.gachibot2.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;



}
