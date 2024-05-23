package com.example.backend.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordLinkRepository extends JpaRepository<PasswordLink, UUID> {
    PasswordLink findByLinkKey(String linkKey);
}
