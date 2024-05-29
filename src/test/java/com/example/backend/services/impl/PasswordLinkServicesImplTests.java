package com.example.backend.services.impl;

import com.example.backend.security.PasswordLink;
import com.example.backend.security.PasswordLinkRepository;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

class PasswordLinkServicesImplTests {

    @Mock
    PasswordLinkRepository passwordLinkRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    PasswordLinkServicesImpl pls;

    User u1 = User.builder().id(UUID.randomUUID()).password("password").build();
    PasswordLink p1 = PasswordLink.builder()
            .linkKey("testLinkKey")
            .alreadyUsed(false)
            .timeSent(LocalDate.now())
            .id(UUID.randomUUID())
            .user(u1)
            .build();


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateCreateNewPasswordLinkShouldGenerateCorrectLink() {
        String newLink = pls.generateCreateNewPasswordLink(p1);

        Assertions.assertEquals("http://localhost:8080/newPassword/" + p1.getLinkKey(), newLink);
    }

    @Test
    void findByLinkKeyShouldFindCorrectPasswordLink() {
        when(pls.findByLinkKey("testLinkKey")).thenReturn(p1);

        PasswordLink p2 = pls.findByLinkKey("testLinkKey");

        Assertions.assertEquals(p1.getLinkKey(), p2.getLinkKey());
        Assertions.assertEquals(p1.isAlreadyUsed(), p2.isAlreadyUsed());
        Assertions.assertEquals(p1.getTimeSent(), p2.getTimeSent());
        Assertions.assertEquals(p1.getId(), p2.getId());
        Assertions.assertEquals(p1.getUser(), p2.getUser());
    }

    @Test
    void saveNewPasswordShouldNotSavePasswordsShorterThanFiveCharacters(){
        String test = pls.saveNewPassword("Mail@mail.se", "hej");

        Assertions.assertEquals(test, "password too short");
    }

    @Test
    void saveNewPasswordShouldSavePasswordsLongerThanFiveCharacters(){
        when(userRepository.getUserByUsername("Mail@mail.se")).thenReturn(u1);
        String test = pls.saveNewPassword("Mail@mail.se", "hejsansvejsan");

        Assertions.assertEquals(test, "success");
        u1.setPassword("password");
    }

    @Test
    void setPasswordLinkToUsedShouldSetPasswordLinkToUsed(){
        pls.setPassWordLinkToUsed(p1);

        Assertions.assertEquals(p1.isAlreadyUsed(), true);
        p1.setAlreadyUsed(false);
    }
}