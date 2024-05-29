package com.example.backend.controller;

import com.example.backend.security.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class UserViewControllerTests {

    @Mock
    private UserRepository userRepo;

    @BeforeEach
    void inIt() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddUser() {
        ArrayList<Role> roles = new ArrayList<>();

        User user = User.builder()
                .username("john.doe@admin.se")
                .password("password")
                .enabled(true)
                .roles(roles)
                .build();
        userRepo.save(user);

        verify(userRepo, times(1)).save(user);

    }

    @Test
    void testDeleteUserById() {
        String username = "jane.doe@recep.se";

        ArrayList<Role> roles = new ArrayList<>();

        User user = User.builder()
                .username(username)
                .password("password")
                .enabled(false)
                .roles(roles)
                .build();

        userRepo.deleteById(user.getId());
        verify(userRepo, times(1)).deleteById(user.getId());


    }
}
