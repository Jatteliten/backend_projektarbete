package com.example.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationPropertiesScan
@Configuration
@ConfigurationProperties(prefix = "integrations")
@Getter
@Setter
public class IntegrationProperties {
    private ShippersProperties shippers;
    private ContractCustomersProperties contractCustomers;
    private BlackListProperties blacklist;
    private EtherealProperties ethereal;
    private EventQueueProperties eventQueue;
}
