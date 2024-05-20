package com.example.backend;

import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FetchEventQueueTestIntegration {

    @Autowired
    CleaningEventRepo cleaningEventRepo;
    @Autowired
    DoorEventRepo doorEventRepo;
    @Autowired
    RoomServices roomServices;

    FetchEventQueue sut;


    @Test
    void fetchAndSaveEventQueueShouldSave() {

    }

    @Test
    void fetchAndSaveEventQueueShouldSaveCorrectly() {

    }

    @Test
    void fetchEventQueueShouldHaveCorrectDataFormat() {

    }
}
