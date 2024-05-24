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
public class EtherealProperties {
    private String name;
    private String userName;
    private String password;

    private String smtpHost;
    private int smtpPort;
    private boolean smtpAuth;
    private boolean starttlsEnable;

}
