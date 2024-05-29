package com.example.backend.services.impl;

import com.example.backend.security.PasswordLink;
import com.example.backend.security.PasswordLinkRepository;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordLinkServicesImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordLinkRepository passwordLinkRepository;

    @InjectMocks
    private PasswordLinkServicesImpl passwordLinkServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNewPasswordShouldSave() {
        String email = "test@example.com";
        String newPassword = "newPassword123";
        User user = User.builder().username(email).build();

        when(userRepository.getUserByUsername(email)).thenReturn(user);

        String result = passwordLinkServices.saveNewPassword(email, newPassword);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(newPassword, savedUser.getPassword()));
        assertEquals("success", result);
    }

    @Test
    void saveNewPasswordShouldNotSaveIfPasswordIsTooShort() {
        String email = "test@example.com";
        String newPassword = "short";

        String result = passwordLinkServices.saveNewPassword(email, newPassword);

        assertEquals("password too short", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveNewPasswordUserDoesNotExist() {
        String email = "test@example.com";
        String newPassword = "newPassword123";

        when(userRepository.getUserByUsername(email)).thenReturn(null);

        String result = passwordLinkServices.saveNewPassword(email, newPassword);

        assertEquals("password too short", result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void generateCreateNewPasswordLinkShouldGenerateCorrectLink() {
        PasswordLink passwordLink = PasswordLink.builder().linkKey("someKey").build();

        String result = passwordLinkServices.generateCreateNewPasswordLink(passwordLink);

        assertEquals("http://localhost:8080/newPassword/someKey", result);
    }

    @Test
    void findByLinkKeyShouldReturnCorrectPasswordLink() {
        String linkKey = "someKey";
        PasswordLink passwordLink = PasswordLink.builder().linkKey(linkKey).build();

        when(passwordLinkRepository.findByLinkKey(linkKey)).thenReturn(passwordLink);

        PasswordLink result = passwordLinkServices.findByLinkKey(linkKey);

        assertEquals(passwordLink, result);
    }

    @Test
    void savePassWordLinkShouldSave() {
        PasswordLink passwordLink = new PasswordLink();

        passwordLinkServices.savePassWordLink(passwordLink);

        verify(passwordLinkRepository).save(passwordLink);
    }

    @Test
    void setPassWordLinkToUsedShouldSetPasswordLinkToUsed() {
        PasswordLink passwordLink = new PasswordLink();

        passwordLinkServices.setPassWordLinkToUsed(passwordLink);

        assertTrue(passwordLink.isAlreadyUsed());
        verify(passwordLinkRepository).save(passwordLink);
    }
}
