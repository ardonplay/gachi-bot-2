package io.github.ardonplay.gachibot2.model;

import java.io.Serializable;

public class BadWordAnduserId implements Serializable {

    private Integer badWordId;

    private Long userId;


    public BadWordAnduserId(Long userId, Integer badWordId) {
        this.userId = userId;
        this.badWordId = badWordId;
    }

}
