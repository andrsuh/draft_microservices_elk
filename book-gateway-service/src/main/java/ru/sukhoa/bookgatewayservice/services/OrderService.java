package ru.sukhoa.bookgatewayservice.services;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.sukhoa.bookgatewayservice.domain.Order;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Order createOrGetExistingOrder(@RequestParam String userName) {
        URI build;
        try {
            build = new URIBuilder("http://order-service/orders")
                    .addParameter("userName", userName).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot build the url ", e);
        }

        return restTemplate.postForObject(build, null, Order.class);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void addDetailsToExistingOrder(@RequestParam int orderId, String bookName, int count) {
        URI build;
        try {
            build = new URIBuilder("http://order-service/orders")
                    .addParameter("orderId", Integer.toString(orderId))
                    .addParameter("bookName", bookName)
                    .addParameter("count", Integer.toString(count))
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot build the url ", e);
        }

        restTemplate.put(build, null);
    }
}
