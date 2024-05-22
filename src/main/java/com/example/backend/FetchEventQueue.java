package com.example.backend;

import com.example.backend.configuration.IntegrationProperties;
import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.model.Room;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomServices;
import events.RoomCleaningFinished;
import events.RoomCleaningStarted;
import events.RoomClosed;
import events.RoomOpened;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@ComponentScan
public class FetchEventQueue implements CommandLineRunner {
    IntegrationProperties integrationProperties;
    private final CleaningEventRepo cr;
    private final DoorEventRepo dr;
    private final RoomServices rs;

    public FetchEventQueue(CleaningEventRepo cr, DoorEventRepo dr, RoomServices rr, IntegrationProperties integrationProperties){
        this.cr = cr;
        this.dr = dr;
        this.rs = rr;
        this.integrationProperties = integrationProperties;
    }

    @Override
    public void run(String... args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(integrationProperties.getEventQueue().getHost());
        factory.setUsername(integrationProperties.getEventQueue().getUserName());
        factory.setPassword(integrationProperties.getEventQueue().getPassword());
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
        channel.basicConsume(integrationProperties.getEventQueue().getQueueName(), true, deliverCallback, consumerTag -> { });
    }

    void saveRoomOpened(RoomEvent roomEvent) {
        dr.save(DoorEvent.builder()
                .type("Opened")
                .timeStamp(roomEvent.TimeStamp)
                .room(getRoom(((RoomOpened) roomEvent).RoomNo)).build());
    }

    void saveRoomClosed(RoomEvent roomEvent) {
        dr.save(DoorEvent.builder()
                .type("Closed")
                .timeStamp(roomEvent.TimeStamp)
                .room(getRoom(((RoomClosed) roomEvent).RoomNo)).build());
    }

    void saveRoomCleaningStarted(RoomEvent roomEvent) {
        cr.save(CleaningEvent.builder()
                .type("Start")
                .timeStamp(roomEvent.TimeStamp)
                .cleaningByUser(((RoomCleaningStarted) roomEvent).CleaningByUser)
                .room(getRoom(((RoomCleaningStarted) roomEvent).RoomNo)).build());
    }

    void saveRoomCleaningFinished(RoomEvent roomEvent) {
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
