package ru.sukhoa.bookservice.configuration;

import io.micrometer.core.lang.NonNull;
import io.micrometer.elastic.ElasticMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.micrometer.core.instrument.Clock.SYSTEM;

@Configuration
public class ElasticConfig {

    @Value("${elastic.hosts}")
    private String elasticHosts;

    @Bean
    public ElasticMeterRegistry elastic() {
        return new ElasticMeterRegistry(new io.micrometer.elastic.ElasticConfig() {
            private final Map<String, String> elasticProperties = new HashMap<String, String>() {
                {
                    put(prefix() + ".hosts", elasticHosts);
                }
            };

            // todo sukhoa: instead of create a map with properties we can just override "hosts" method.
            @Override
            public String get(String key) {
                return elasticProperties.get(key);
            }

            @Override
            @NonNull
            public Duration step() {
                return Duration.ofSeconds(3);
            }

            @Override
            public String index() {
                return "/metrics";
            }
        }, SYSTEM);
    }
}
