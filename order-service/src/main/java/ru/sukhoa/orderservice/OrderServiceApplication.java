package ru.sukhoa.orderservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import ru.sukhoa.orderservice.domain.User;
import ru.sukhoa.orderservice.domain.UserRepository;

import java.util.stream.Stream;

@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApplication {

    @Bean
    CommandLineRunner cmlr( UserRepository userRepository ) {
        return args -> {
            Stream.of( "user_1", "user_2", "user_3" )
                    .map( User::new )
                    .forEach( userRepository::save );
        };
    }

    public static void main( String[] args ) {
        SpringApplication.run( OrderServiceApplication.class, args );
    }
}
