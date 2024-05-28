package com.example.backend.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContractCustomersProperties {
    private String url;
    private String contractCustomerTestUrl;
}
