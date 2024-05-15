package com.example.backend.repos;

import com.example.backend.model.modelUti.DoorEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoorEventRepo extends JpaRepository<DoorEvent, Long> {
    List<DoorEvent> findByRoomId(Long id);
}
