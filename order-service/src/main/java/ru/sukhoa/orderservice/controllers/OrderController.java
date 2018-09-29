package ru.sukhoa.orderservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.orderservice.domain.Order;
import ru.sukhoa.orderservice.domain.OrderDetails;
import ru.sukhoa.orderservice.domain.OrderRepository;
import ru.sukhoa.orderservice.domain.User;
import ru.sukhoa.orderservice.domain.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrderController {
    private static final Logger LOGGER = LogManager.getLogger(OrderController.class);

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Order createOrder(@RequestParam String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User doesn't exists " + userName);
        }

        Order order = orderRepository.findByUsername(userName).stream()
                .filter(Order::isCollecting)
                .findFirst()
                .orElse(orderRepository.save(new Order(user.getId())));

        LOGGER.info("Return order {} for user {} ", order.getId(), user.getName());
        return order;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void addDetails(@RequestParam int orderId, @RequestParam int bookId, @RequestParam int count) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new IllegalArgumentException("No such order exists : " + orderId);
        }
        order.get().details.add(new OrderDetails(orderId, bookId, count));
        orderRepository.save(order.get());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Order getOrderById(@RequestParam int orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new IllegalArgumentException("No such order exists : " + orderId);
        }
        return order.get();
    }

    @RequestMapping(value = "byUserName", method = RequestMethod.GET)
    public List<Order> getOrdersByUserName(@RequestParam String userName) {
        return orderRepository.findByUsername(userName);
    }
}

