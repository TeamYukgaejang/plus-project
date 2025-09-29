package org.example.plusproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PlusProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlusProjectApplication.class, args);
    }

}
