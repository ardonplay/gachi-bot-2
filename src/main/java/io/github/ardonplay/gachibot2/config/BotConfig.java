package io.github.ardonplay.gachibot2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BotConfig {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.message_length}")
    private int messageLength;
}
