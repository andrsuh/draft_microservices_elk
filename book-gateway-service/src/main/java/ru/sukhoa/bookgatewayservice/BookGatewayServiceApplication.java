package ru.sukhoa.bookgatewayservice;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.lang.NonNull;
import io.micrometer.elastic.ElasticConfig;
import io.micrometer.elastic.ElasticMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import static io.micrometer.core.instrument.Clock.SYSTEM;


//@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
public class BookGatewayServiceApplication {
    private static final Logger logger = LogManager.getLogger(BookGatewayServiceApplication.class);

    @Bean
    CommandLineRunner runner() {
        return (args) -> {
            logger.info("Runner started. Application is starting");
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(BookGatewayServiceApplication.class, args);
    }
}
