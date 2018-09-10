package ru.sukhoa.bookgatewayservice;

import io.micrometer.core.instrument.Clock;
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
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.web.client.RestTemplate;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import ru.sukhoa.bookgatewayservice.controllers.BooksGatewayController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import static io.micrometer.core.instrument.Clock.*;


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

//    @Bean
//    @ExportMetricWriter
//    MetricWriter metricWriter(MBeanExporter exporter) {
//        return new JmxMetricWriter(exporter);
//    }

    public static void main( String[] args ) {
        SpringApplication.run( BookGatewayServiceApplication.class, args );

        CompositeMeterRegistry registry = new CompositeMeterRegistry();
        registry.add(elastic());

        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
        registry.gauge("my.gauge", integers, ArrayList::size);

        new JvmMemoryMetrics().bindTo(registry);

        Counter counter = registry.counter("my.counter.new");
        Counter counter2 = Counter.builder("my.counter.new.2")
                .tag("tag", "pong")
                .register(registry);

        counter2.increment();
        counter2.increment();
        counter2.increment();

//        Disposable subscribe = Flux.generate((s) -> s.next(1))
//                .delayElements(Duration.ofSeconds(4))
//                .doOnEach(i -> counter.increment())
//                .subscribe((i) -> System.out.println(counter.count()));

        counter.increment();
        counter.increment();
        counter.increment();

        System.out.println(counter.count());
//        new JvmMemoryMetrics().bindTo(registry);
    }

    @Bean
    JvmThreadMetrics threadMetrics(){
        return new JvmThreadMetrics();
    }

    private static ElasticMeterRegistry elastic(){
        return new ElasticMeterRegistry(new ElasticConfig() {
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

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
