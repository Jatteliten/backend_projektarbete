package com.example.backend.repos;

import com.example.backend.model.ThymeLeafTemplates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThymeleafTemplateRepo extends JpaRepository<ThymeLeafTemplates, Long> {
    Optional<ThymeLeafTemplates> findByTitle(String title);
}
