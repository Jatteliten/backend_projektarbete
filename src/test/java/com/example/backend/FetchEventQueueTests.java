package com.example.backend;

import com.example.backend.model.Room;
import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.*;
import events.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FetchEventQueueTests {

    @Mock
    private CleaningEventRepo cleaningEventRepo;

    @Mock
    private DoorEventRepo doorEventRepo;

    @Mock
    private RoomServices roomServices;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection connection;

    @Mock
    private Channel channel;

    @InjectMocks
    private FetchEventQueue fetchEventQueue;

    Room room;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException, TimeoutException {
        MockitoAnnotations.openMocks(this);
        room = new Room();
        room.setId(1L);
        when(roomServices.findById(1L)).thenReturn(room);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(connectionFactory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
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

    /* FUNKAR EJ
    @Test
    void testRunMethodWithCorrectDataFormat() throws Exception {
        MockitoAnnotations.openMocks(this);
        String jsonData = "{\"type\":\"RoomOpened\",\"TimeStamp\":\"2024-05-20T02:29:37.933632893\",\"RoomNo\":\"1\"}";

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        when(objectMapper.readValue(any(String.class), eq(RoomEvent.class))).thenReturn(new RoomOpened("1", LocalDateTime.parse("2024-05-20T02:29:37.933632893")));

        ArgumentCaptor<DeliverCallback> deliverCallbackCaptor = ArgumentCaptor.forClass(DeliverCallback.class);
        doNothing().when(channel).basicConsume(anyString(), anyBoolean(), deliverCallbackCaptor.capture(), (CancelCallback) any());

        fetchEventQueue.run();

        DeliverCallback deliverCallback = deliverCallbackCaptor.getValue();
        assertThrows(IOException.class, () -> deliverCallback.handle("consumerTag", new Delivery(null, null, jsonData.getBytes(StandardCharsets.UTF_8))));

    }

     */

    /* FUNKAR EJ
    @Test
    void testRunMethodWithCorrectDataFormat() throws Exception{
        doAnswer(invocation -> {
            String consumerTag = invocation.getArgument(0);
            DeliverCallback deliverCallback = invocation.getArgument(2);
            byte[] messageBytes = "{\"type\":\"RoomOpened\",\"RoomNo\":\"1\",\"TimeStamp\":\"2023-01-01T12:00:00\"}".getBytes(StandardCharsets.UTF_8);
            Delivery delivery = new Delivery(null, null, messageBytes);
            deliverCallback.handle(consumerTag, delivery);
            return null;
        }).when(channel).basicConsume(anyString(), anyBoolean(), any(DeliverCallback.class), any(CancelCallback.class));

        fetchEventQueue.run();

        ArgumentCaptor<DoorEvent> doorEventCaptor = ArgumentCaptor.forClass(DoorEvent.class);
        verify(doorEventRepo).save(doorEventCaptor.capture());

        DoorEvent savedDoorEvent = doorEventCaptor.getValue();
        assertEquals("Opened", savedDoorEvent.getType());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), savedDoorEvent.getTimeStamp());

    }

     */
}
