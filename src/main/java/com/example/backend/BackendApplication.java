package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        if(args.length == 0) {
            SpringApplication.run(BackendApplication.class, args);
        } else if(Objects.equals(args[0], "fetchcontractcustomers")){
            SpringApplication application = new SpringApplication(FetchContractCustomers.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "fetchshippers")){
            SpringApplication application = new SpringApplication(FetchShippers.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "fetcheventqueue")){
            SpringApplication application = new SpringApplication(FetchEventQueue.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "bean")){
            SpringApplication application = new SpringApplication(BeanInitialisation.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "fetchusers")){
            SpringApplication application = new SpringApplication(FetchUsers.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        }
    }


}