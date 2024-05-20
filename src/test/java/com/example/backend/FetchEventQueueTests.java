package com.example.backend;

import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FetchEventQueueTests {

    @Mock
    private CleaningEventRepo cleaningEventRepo;

    @Mock
    private DoorEventRepo doorEventRepo;

    @Mock
    private RoomServices roomServices;

    @InjectMocks
    private FetchEventQueue fetchEventQueue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    Ska man skapa upp instanser av RoomEvent eller de mer specifika subklasserna
    till de olika testmetoderna?
     */

    @Test
    void testSaveRoomOpened() {

    }

    @Test
    void testSaveRoomClosed() {

    }

    @Test
    void testSaveDoorOpened() {

    }

    @Test
    void testSaveDoorClosed() {

    }
}
