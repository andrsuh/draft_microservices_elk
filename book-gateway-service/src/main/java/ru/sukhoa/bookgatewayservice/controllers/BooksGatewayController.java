package ru.sukhoa.bookgatewayservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
import ru.sukhoa.bookgatewayservice.services.BookService;
import ru.sukhoa.bookgatewayservice.services.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController("api/orders")
public class BooksGatewayController {
    private static final Logger LOGGER = LogManager.getLogger(BooksGatewayController.class);

    private final RestTemplate restTemplate;

    private final BookService bookService;

    private final OrderService orderService;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public BooksGatewayController(RestTemplate restTemplate, BookService bookService, OrderService orderService, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.bookService = bookService;
        this.orderService = orderService;
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<String> getBooksNames() {
        List<ServiceInstance> instances = discoveryClient.getInstances("book-service");

        ParameterizedTypeReference<Resources<Book>> res = new ParameterizedTypeReference<Resources<Book>>() {
        };

        return this.restTemplate.exchange("http://book-service/books", HttpMethod.GET, null, res)
                .getBody().getContent().stream()
                .map(b -> b.name)
                .collect(Collectors.toList());
    }

    /**
     * Creates new order and adds a first purchase to the shopping card
     *
     * @return the newly created order/shopping card
     */
    @RequestMapping(method = RequestMethod.POST)
    public Order createShoppingCard(@RequestParam String userName, @RequestParam String bookName, @RequestParam int count) {
        if (count < 1) {
            throw new IllegalArgumentException("You cannot to order less than one book");
        }

        // book needed books
        bookService.reserveBook(bookName, count);
        Order order = orderService.createOrGetExistingOrder(userName);
        this.addItemToExistingOrder(order.id, bookName, count);

        return order;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void addItemToExistingOrder(@RequestParam int orderId, @RequestParam String bookName, @RequestParam int count) {
        if (count < 1) {
            throw new IllegalArgumentException("You cannot to order less than one book");
        }

        // book needed books
        bookService.reserveBook(bookName, count);
        orderService.addDetailsToExistingOrder(orderId, bookName, count);
    }
}
