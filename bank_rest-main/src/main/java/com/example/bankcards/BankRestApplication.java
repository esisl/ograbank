package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

@SpringBootApplication
public class BankRestApplication {
    public static void main(String[] args) {
        //SpringApplication.run(BankRestApplication.class, args);
        SpringApplication app = new SpringApplication(BankRestApplication.class);
    
        // ⬇️ ПРОВЕРКА ПРОФИЛЯ ⬇️
        app.addListeners(event -> {
            if (event instanceof ApplicationEnvironmentPreparedEvent e) {
                String active = e.getEnvironment().getProperty("spring.profiles.active");
            }
        });
        // ⬆️ КОНЕЦ ПРОВЕРКИ ⬆️
        
        app.run(args);
    }
}