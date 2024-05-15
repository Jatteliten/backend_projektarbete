package com.example.backend.repos;

import com.example.backend.model.modelUti.CleaningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleaningEventRepo extends JpaRepository<CleaningEvent, Long> {
}
