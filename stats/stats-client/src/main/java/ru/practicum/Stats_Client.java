package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Stats_Client {
    public static void main(String[] args) {
        SpringApplication.run(Stats_Dto.class, args);
    }
}
