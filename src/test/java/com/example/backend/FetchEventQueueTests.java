package com.example.backend;

import com.example.backend.events.RoomCleaningFinished;
import com.example.backend.events.RoomCleaningStarted;
import com.example.backend.events.RoomClosed;
import com.example.backend.events.RoomOpened;
import com.example.backend.model.Room;
import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FetchEventQueueTests {

    @Mock
    private CleaningEventRepo cleaningEventRepo;

    @Mock
    private DoorEventRepo doorEventRepo;

    @Mock
    private RoomServices roomServices;

    @InjectMocks
    private FetchEventQueue fetchEventQueue;

    Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        room = new Room();
        room.setId(1L);
        when(roomServices.findById(1L)).thenReturn(room);
    }


    @Test
    void saveRoomOpenedShouldSave() {
        RoomOpened roomOpened = new RoomOpened();
        roomOpened.RoomNo = "1";
        roomOpened.TimeStamp = LocalDateTime.now();

        fetchEventQueue.saveRoomOpened(roomOpened);

        //ArgumentCaptor fångar de argument som skickas till en mockad metod.
        //Här förbereds alltså en 'capture' för DoorEvent-objekt.
        ArgumentCaptor<DoorEvent> doorEventCaptor = ArgumentCaptor.forClass(DoorEvent.class);

        //Verifiera att metod hos doorEventRepo - mocken har anropats.
        verify(doorEventRepo).save(doorEventCaptor.capture());

        //Undersök det 'fångade' objektet
        DoorEvent savedDoorEvent = doorEventCaptor.getValue();
        assertEquals("Opened", savedDoorEvent.getType());
        assertEquals(roomOpened.TimeStamp, savedDoorEvent.getTimeStamp());
        assertEquals(room, savedDoorEvent.getRoom());
    }

    @Test
    void saveRoomClosedShouldSave() {
        RoomClosed roomClosed = new RoomClosed();
        roomClosed.RoomNo = "1";
        roomClosed.TimeStamp = LocalDateTime.now();

        fetchEventQueue.saveRoomClosed(roomClosed);

        ArgumentCaptor<DoorEvent> doorEventCaptor = ArgumentCaptor.forClass(DoorEvent.class);
        verify(doorEventRepo).save(doorEventCaptor.capture());

        DoorEvent savedDoorEvent = doorEventCaptor.getValue();
        assertEquals("Closed", savedDoorEvent.getType());
        assertEquals(roomClosed.TimeStamp, savedDoorEvent.getTimeStamp());
        assertEquals(room, savedDoorEvent.getRoom());
    }

    @Test
    void saveRoomCleaningStartedShouldSave() {
        RoomCleaningStarted roomCleaningStarted = new RoomCleaningStarted();
        roomCleaningStarted.RoomNo = "1";
        roomCleaningStarted.TimeStamp = LocalDateTime.now();
        roomCleaningStarted.CleaningByUser = "User1";

        fetchEventQueue.saveRoomCleaningStarted(roomCleaningStarted);

        ArgumentCaptor<CleaningEvent> cleaningEventCaptor = ArgumentCaptor.forClass(CleaningEvent.class);
        verify(cleaningEventRepo).save(cleaningEventCaptor.capture());

        CleaningEvent savedCleaningEvent = cleaningEventCaptor.getValue();
        assertEquals("Start", savedCleaningEvent.getType());
        assertEquals(roomCleaningStarted.TimeStamp, savedCleaningEvent.getTimeStamp());
        assertEquals(roomCleaningStarted.CleaningByUser, savedCleaningEvent.getCleaningByUser());
        assertEquals(room, savedCleaningEvent.getRoom());

    }

    @Test
    void saveRoomCleaningFinishedShouldSave() {
        RoomCleaningFinished roomCleaningFinished = new RoomCleaningFinished();
        roomCleaningFinished.RoomNo = "1";
        roomCleaningFinished.TimeStamp = LocalDateTime.now();
        roomCleaningFinished.CleaningByUser = "User1";

        fetchEventQueue.saveRoomCleaningFinished(roomCleaningFinished);

        ArgumentCaptor<CleaningEvent> cleaningEventCaptor = ArgumentCaptor.forClass(CleaningEvent.class);
        verify(cleaningEventRepo).save(cleaningEventCaptor.capture());

        CleaningEvent savedCleaningEvent = cleaningEventCaptor.getValue();
        assertEquals("Finished", savedCleaningEvent.getType());
        assertEquals(roomCleaningFinished.TimeStamp, savedCleaningEvent.getTimeStamp());
        assertEquals(roomCleaningFinished.CleaningByUser, savedCleaningEvent.getCleaningByUser());
        assertEquals(room, savedCleaningEvent.getRoom());
    }

}
