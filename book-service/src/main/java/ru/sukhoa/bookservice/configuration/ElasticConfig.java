package ru.sukhoa.bookservice.configuration;

import io.micrometer.core.lang.NonNull;
import io.micrometer.elastic.ElasticMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.micrometer.core.instrument.Clock.SYSTEM;

@Configuration
public class ElasticConfig {

    @Bean
    public static ElasticMeterRegistry elastic() {
        return new ElasticMeterRegistry(new io.micrometer.elastic.ElasticConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            @NonNull
            public Duration step() {
                return Duration.ofSeconds(5);
            }

            @Override
            public String index() {
                return "/metrics";
            }

        }, SYSTEM);
    }
}
