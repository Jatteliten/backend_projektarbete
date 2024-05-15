package com.example.backend.repos;

import com.example.backend.model.DoorEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoorEventRepo extends JpaRepository<DoorEvent, Long> {
}
