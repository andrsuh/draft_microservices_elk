package ru.sukhoa.bookgatewayservice.controllers;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.sukhoa.bookgatewayservice.domain.Book;
import ru.sukhoa.bookgatewayservice.domain.Order;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController("api/orders")
public class BooksGatewayController {
    private static final Logger logger = LogManager.getLogger(BooksGatewayController.class);

    private final RestTemplate restTemplate;

    @Autowired
    public BooksGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<String> getBooksNames() {
        ParameterizedTypeReference<Resources<Book>> res = new ParameterizedTypeReference<Resources<Book>>() {
        };

        return this.restTemplate.exchange("http://book-service/books", HttpMethod.GET, null, res)
                .getBody().getContent().stream()
                .map(b -> b.name)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Order createOrder(@RequestParam String userName, @RequestParam String bookName, @RequestParam int count) {
        logger.info("Create order called");

        if (count < 1) {
            throw new IllegalArgumentException("You cannot to order less than one book");
        }

        // book needed books
        URI build;
        try {
            build = new URIBuilder("http://book-service/books")
                    .addParameter("bookName", bookName)
                    .addParameter("count", Integer.toString(-count)).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot build the url ", e);
        }

        // create an order
        restTemplate.put(build, null);
        try {
            build = new URIBuilder("http://order-service/orders")
                    .addParameter("userName", userName).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot build the url ", e);
        }

        return restTemplate.postForObject(build, null, Order.class);
    }
}
