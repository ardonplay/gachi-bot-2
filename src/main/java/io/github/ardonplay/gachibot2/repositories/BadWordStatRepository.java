package io.github.ardonplay.gachibot2.repositories;

import io.github.ardonplay.gachibot2.model.BadWordStat;
import org.springframework.data.repository.CrudRepository;

public interface BadWordStatRepository extends CrudRepository<BadWordStat, Long> { }
