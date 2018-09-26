package ru.sukhoa.bookgatewayservice.controllers;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class InfrastructureMetricsConfig {

    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics(
            @Value("${eureka.instance.instance-id}") String applicationId) {
        return new JvmMemoryMetrics(Collections.singletonList(new ImmutableTag("application_id", applicationId)));
    }
}
