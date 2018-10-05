package ru.sukhoa.bookservice;

import com.zaxxer.hikari.util.ClockSource;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.lang.NonNull;
import io.micrometer.elastic.ElasticMeterRegistry;
import io.micrometer.jmx.JmxConfig;
import io.micrometer.jmx.JmxMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.sukhoa.bookservice.domain.Book;
import ru.sukhoa.bookservice.domain.BookRepository;
import ru.sukhoa.bookservice.services.BookService;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@SpringBootApplication
@EnableEurekaClient
public class BookServiceApplication {
    private static final Logger LOGGER = LogManager.getLogger(BookServiceApplication.class);

    @Bean
    CommandLineRunner cmlr(BookRepository repository) {
        return args -> {
            Stream.of("Book1", "Book2", "Book3", "Book4", "Book5", "Book6")
                    .map(name -> new Book(name, 5))
                    .forEach(repository::save);
        };
    }

    // https://micrometer.io/docs/installing
    @Bean
    CommandLineRunner meterRegistryExample(ElasticMeterRegistry elasticMeterRegistry) {
        return args -> {
            // Allows us to push metrics to more than one monitoring system.
            CompositeMeterRegistry registry = new CompositeMeterRegistry();

            // Until some registry is in composite registry the increments of the counter will not be applied (or visible)
            registry.add(elasticMeterRegistry);

            // two ways to create a Meter
            Counter first = registry.counter("my.counter", "tag", "1"); // class Tag useful for working with tags
            Counter second = Counter.builder("my.counter")
                    .tag("tag", "2")
                    .register(registry);

            Flux.interval(Duration.ofSeconds(1))
                    .doOnNext((n) -> {
                        // we are incrementing counters for all of the registers in the composite registry
//                        first.increment();
//                        LOGGER.info("counter : {}, tag : {} incremented, value = {}",
//                                first.getId().getName(), first.getId().getTags(), first.count());

//                        second.increment(2);
                        LOGGER.info("counter : {}, tag : {} incremented, value = {}",
                                second.getId().getName(), second.getId().getTags(), second.count());
                    }).subscribe();
        };
    }

    @Bean
    CommandLineRunner globalMeterRegistryExample(MeterRegistry registry) {
        return args -> {

            // class Metrics contains global composite registry
            // management.metrics.use-global-registry=false # true by default
            Counter first = Metrics.counter("my.counter", "tag", "1");

            Counter second = Counter.builder("my.counter")
                    .tag("tag", "2")
                    .register(Metrics.globalRegistry);

            Counter third = Counter.builder("my.counter")
                    .tag("tag", "3")
                    .register(registry);

            // Export metrics to monitoring system (Elasticsearch, jmx, Atlas...)
            // management.metrics.export.{system}.enabled=false # true by default
            // It should be in classpath
            Metrics.addRegistry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));

            Flux.interval(Duration.ofSeconds(1))
                    .doOnNext((n) -> {
                        first.increment();
                        LOGGER.info("counter : {}, tag : {} incremented, value = {}",
                                first.getId().getName(), first.getId().getTags(), first.count());

                        second.increment(2);
                        // getting certain metric by searching It in the registry
//                        Counter secondCounterRef = Metrics.counter("my.counter", "tag", "2");
//                        LOGGER.info(secondCounterRef == second);
//                        LOGGER.info("counter : {}, tag : {} incremented, value = {}",
//                                secondCounterRef.getId().getName(), secondCounterRef.getId().getTags(), secondCounterRef.count());

//                        third.increment(4);
//                        LOGGER.info("counter : {}, tag : {} incremented, value = {}",
//                                third.getId().getName(), third.getId().getTags(), third.count());
                    }).subscribe();
        };
    }

    @Bean
    CommandLineRunner gaugesExample(MeterRegistry registry) {
        return args -> {
            Metrics.addRegistry(new JmxMeterRegistry(JmxConfig.DEFAULT, Clock.SYSTEM));

            // Gauges are useful for monitoring things with natural upper bounds. We don’t recommend using a
            // gauge to monitor things like request count, as they can grow without bound for the duration of
            // an application instance’s life.

            // Never gauge something you can count with a Counter!

            // we can accept-decline-transform meters
            Metrics.globalRegistry.config().meterFilter(new MeterFilter() {
                @Override
                @NonNull
                public Meter.Id map(@NonNull Meter.Id id) {
                    // I know that Egor don't like more than one return per function :(
                    if (id.getName().startsWith("libs")) {
                        return id.withName(id.getName())
                                .withTag(Tag.of("timestamp", Calendar.getInstance().getTime().toString()));
                    }
                    return id;
                }
            });

            Metrics.globalRegistry.config().meterFilter(new MeterFilter() {
                @Override
                @NonNull
                public MeterFilterReply accept(@NonNull Meter.Id id) {
                    if (id.getName().startsWith("bad.metric")) {
                        return MeterFilterReply.DENY;
                    }
                    return MeterFilterReply.ACCEPT;
                }
            });

            // Static (Oooh GOD!!!) methods facilitate meter filters creating
            // in Spring Boot it is enough to define Beans returning filters. But It will not be applied to binders
            // if we want to apply filter to binders we should use MeterRegistryCustomizer
            MeterFilter.denyNameStartsWith("hello.");
            MeterFilter.denyUnless((meterId) -> true);
            MeterFilter.ignoreTags("one", "twoo", "threee");
            MeterFilter.commonTags(Tags.of(Tag.of("hop", "hey"), Tag.of("hop", "lalaley")));
            MeterFilter.renameTag("meterPrefix", "nameFromTag", "newTagName");


            // tracking atomic value. It can go up ind down
            AtomicLong jenkinsBuildNum = Metrics.gauge("libs.gauge",
                    Collections.singletonList(Tag.of("timestamp", "empty")), new AtomicLong(), AtomicLong::doubleValue);

            jenkinsBuildNum = null; // now is available for gc
            System.gc();

            // return held object
            List<Object> trackedList = Metrics.gaugeCollectionSize("my_another_gauge", Tags.empty(), new ArrayList<>());

            // returns Gauge
            Gauge.builder("my_gauge", trackedList, List::size)
                    .tags(Tags.empty())
                    .description("My gauge")
                    .register(Metrics.globalRegistry);

            Flux.interval(Duration.ofSeconds(3))
                    .doOnNext((n) -> {
                        // numerous gauge
                        // jenkinsBuildNum.incrementAndGet();

                        // collection size gauge
                        if (Math.random() < 0.95) {
                            //noinspection ConstantConditions
                            trackedList.add(new Object());
                        } else {
                            //noinspection ConstantConditions
                            trackedList.clear();
                        }
                    }).subscribe();
        };
    }

    @Bean
    CommandLineRunner timersExample(MeterRegistry registry, BookService bookService, NestedClass autowiredNestedClass) {
        return args -> {
            Timer timer = Timer.builder("my.timer")
                    .tags(Tags.empty())
                    .publishPercentileHistogram(true)
                    // create 5 buckets
                    .sla(Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(300), Duration.ofMillis(400))
                    .publishPercentiles(0.25, 0.50, 0.75, 0.95, 0.99)
                    .description("My timer")
                    .register(Metrics.globalRegistry);

            // recording runnable/callable
            Flux.interval(Duration.ofSeconds(3))
                    .doOnNext((n) -> {
                        timer.record(() -> {
                            try {
                                Thread.sleep((long) ((Math.random() + 0.5) * 100));
                            } catch (InterruptedException ignored) {
                            }
                        });
                    }).subscribe();

            // recording block of code
            Flux.interval(Duration.ofSeconds(3))
                    .doOnNext((n) -> {
                        Timer.Sample sample = Timer.start();
                        try {
                            Thread.sleep((long) ((Math.random() + 1) * 100));
                        } catch (InterruptedException ignored) {
                        }

                        sample.stop(Metrics.globalRegistry.timer("my.second.timer"));
                    }).subscribe();

            NestedClass nestedClass = new NestedClass(); // not a bean
            // recording method
            Flux.interval(Duration.ofSeconds(1))
                    .doOnNext((n) -> {
                        nestedClass.recordingMethod(); // doesn't work
                        autowiredNestedClass.recordingMethod(); // It works
                        bookService.recordingMethod(); // It works
                    }).subscribe();
        };
    }

//    @Bean
//    public NestedClass nestedClassBean() {
//        return new NestedClass();
//    }

    private static class NestedClass {
        @Timed(value = "my_third_timer", extraTags = {"hello", "bambaleyo"}, histogram = true, percentiles = {0.25, 0.5, 0.75})
        void recordingMethod() {
            try {
                Thread.sleep((long) ((Math.random() + 0.5) * 100));
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }
}
