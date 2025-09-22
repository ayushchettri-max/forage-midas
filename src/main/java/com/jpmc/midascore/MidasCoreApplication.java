package com.jpmc.midascore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka  // <-- Add this to enable Kafka support
public class MidasCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MidasCoreApplication.class, args);
    }
}
