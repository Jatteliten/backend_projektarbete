package com.example.backend;

import com.example.backend.model.CleaningEvent;
import com.example.backend.model.DoorEvent;
import com.example.backend.model.Room;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.repos.RoomRepo;
import com.example.backend.services.RoomServices;
import events.RoomCleaningFinished;
import events.RoomCleaningStarted;
import events.RoomClosed;
import events.RoomOpened;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import events.RoomEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@ComponentScan
public class FetchEventQueue implements CommandLineRunner {

    private static final String QUEUE_NAME = "43a6c52f-8237-4a29-801f-6cbc1c8fd2d1";
    private final CleaningEventRepo cr;
    private final DoorEventRepo dr;
    private final RoomServices rs;
    public FetchEventQueue(CleaningEventRepo cr, DoorEventRepo dr, RoomServices rr){
        this.cr = cr;
        this.dr = dr;
        this.rs = rr;
    }

    @Override
    public void run(String... args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("128.140.81.47");
        factory.setUsername("djk47589hjkew789489hjf894");
        factory.setPassword("sfdjkl54278frhj7");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            RoomEvent roomEvent = mapper.readValue(message, RoomEvent.class);
            if(roomEvent instanceof RoomCleaningFinished){
                saveRoomCleaningFinished(roomEvent);
            }else if(roomEvent instanceof RoomCleaningStarted){
                saveRoomCleaningStarted(roomEvent);
            }else if(roomEvent instanceof RoomClosed){
                saveRoomClosed(roomEvent);
            }else if(roomEvent instanceof RoomOpened){
                saveRoomOpened(roomEvent);
            }
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    private void saveRoomOpened(RoomEvent roomEvent) {
        dr.save(DoorEvent.builder()
                .type("Opened")
                .timeStamp(roomEvent.TimeStamp)
                .room(getRoom(((RoomOpened) roomEvent).RoomNo)).build());
    }

    private void saveRoomClosed(RoomEvent roomEvent) {
        dr.save(DoorEvent.builder()
                .type("Closed")
                .timeStamp(roomEvent.TimeStamp)
                .room(getRoom(((RoomClosed) roomEvent).RoomNo)).build());
    }

    private void saveRoomCleaningStarted(RoomEvent roomEvent) {
        cr.save(CleaningEvent.builder()
                .type("Start")
                .timeStamp(roomEvent.TimeStamp)
                .cleaningByUser(((RoomCleaningStarted) roomEvent).CleaningByUser)
                .room(getRoom(((RoomCleaningStarted) roomEvent).RoomNo)).build());
    }

    private void saveRoomCleaningFinished(RoomEvent roomEvent) {
        cr.save(CleaningEvent.builder()
                .type("Finished")
                .timeStamp(roomEvent.TimeStamp)
                .cleaningByUser(((RoomCleaningFinished) roomEvent).CleaningByUser)
                .room(getRoom(((RoomCleaningFinished) roomEvent).RoomNo)).build());
    }

    private Room getRoom(String roomId){
        try {
            return rs.findById(Long.parseLong(roomId));
        } catch(NumberFormatException e){
            return null;
        }
    }
}
