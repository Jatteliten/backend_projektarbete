package com.example.backend.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String linkKey;
    LocalDate timeSent;
    private boolean alreadyUsed;

    @ManyToOne
    User user;
}
