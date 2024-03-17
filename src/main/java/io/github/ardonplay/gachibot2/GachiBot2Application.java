package io.github.ardonplay.gachibot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GachiBot2Application {

    public static void main(String[] args) {
        SpringApplication.run(GachiBot2Application.class, args);
    }

}
