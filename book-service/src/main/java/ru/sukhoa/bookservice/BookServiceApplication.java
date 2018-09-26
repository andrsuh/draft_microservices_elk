package ru.sukhoa.bookservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import ru.sukhoa.bookservice.domain.Book;
import ru.sukhoa.bookservice.domain.BookRepository;

import java.util.stream.Stream;

@SpringBootApplication
@EnableEurekaClient
public class BookServiceApplication {

    @Bean
    CommandLineRunner cmlr(BookRepository repository) {
        return args -> {
            Stream.of("Book1", "Book2", "Book3", "Book4", "Book5", "Book6")
                    .map(name -> new Book(name, 5))
                    .forEach(repository::save);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }
}
