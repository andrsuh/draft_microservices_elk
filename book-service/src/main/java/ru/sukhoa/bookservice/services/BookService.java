package ru.sukhoa.bookservice.services;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

@Service
public class BookService {

//    @Timed(value = "my_third_timer", extraTags = {"hello", "hello"}, histogram = true, percentiles = {0.25, 0.5, 0.75})
    public void recordingMethod() {
        try {
            Thread.sleep((long) ((Math.random() + 0.5) * 100));
        } catch (InterruptedException ignored) {
        }
    }
}
