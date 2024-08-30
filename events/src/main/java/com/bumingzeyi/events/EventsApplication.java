package com.bumingzeyi.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :BuMing
 * @date : 2022-04-02 21:04
 */
@SpringBootApplication(scanBasePackages = "com.bumingzeyi")
public class EventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventsApplication.class, args);
    }

}
