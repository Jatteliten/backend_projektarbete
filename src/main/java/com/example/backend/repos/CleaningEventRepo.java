package com.example.backend.repos;

import com.example.backend.model.modelUti.CleaningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CleaningEventRepo extends JpaRepository<CleaningEvent, Long> {
    List<CleaningEvent> findByRoomId(Long roomId);

}
