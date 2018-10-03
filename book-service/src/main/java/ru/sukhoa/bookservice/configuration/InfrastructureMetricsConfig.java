package ru.sukhoa.bookservice.configuration;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class InfrastructureMetricsConfig {

    @Bean
    public MeterRegistryCustomizer metersCustomizer(@Value("${eureka.instance.instance-id}") String applicationId) {
        return (registry) ->
                registry.config()
                        .namingConvention(NamingConvention.dot)
                        .commonTags(Collections.singletonList(Tag.of("application_id", applicationId)));
    }

    @Bean
    public MeterFilter denyActuatorEndpointsReporting() {
        return new MeterFilter() {
            @Override
            @NonNull
            public MeterFilterReply accept(@NonNull Meter.Id id) {
                if (id.getName().startsWith("http.server.requests")) {
                    String tagValue = id.getTag("uri");
                    if (tagValue != null && tagValue.startsWith("/actuator")) {
                        return MeterFilterReply.DENY;
                    }
                }
                return MeterFilterReply.ACCEPT;
            }
        };
    }

//    @Bean
//    public MeterFilter denyMyMetricsReporting() {
//        return MeterFilter.denyNameStartsWith("my");
////        return MeterFilter.deny(id -> id.getName().startsWith("my"));
////        return MeterFilter.ignoreTags("tag");
//    }

    @Bean
    public MeterFilter transformMyMetricsReporting() {
        return new MeterFilter() {
            @Override
            @NonNull
            public Meter.Id map(@NonNull Meter.Id id) {
                if (id.getName().startsWith("my")) {
                    return id.withName(id.getName())
                            .withTag(Tag.of("tag", Optional.ofNullable(id.getTag("tag")).orElse("")))
                            .withTag(Tag.of("extra_tag", "enriched"));
                }
                return id;
            }
        };
    }
}
