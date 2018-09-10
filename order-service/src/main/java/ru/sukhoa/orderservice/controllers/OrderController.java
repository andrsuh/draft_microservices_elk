package ru.sukhoa.orderservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.orderservice.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "orders" )
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping( method = RequestMethod.POST )
    public Order createOrder( @RequestParam String userName ) {
        User user = userRepository.findByName( userName );
        if ( user == null ) {
            throw new IllegalArgumentException( "User doesn't exists" );
        }

        return orderRepository.save( new Order( user ) );
    }

    @RequestMapping( method = RequestMethod.PUT )
    public void addDetails( @RequestParam int orderId, @RequestParam int bookId, @RequestParam int count ) {
        Order order = orderRepository.findById( orderId ).get();
//        if ( order == null ) {
//            throw new IllegalArgumentException( "No such order exists : " + orderId );
//        }
        order.details.add( new OrderDetails( orderId, bookId, count ) );
        orderRepository.save( order );
    }

    @RequestMapping( method = RequestMethod.GET )
    public Order getOrderById( @RequestParam int orderId ) {
        Order order = orderRepository.findById( orderId ).get();
//        if ( order == null ) {
//            throw new IllegalArgumentException( "No such order exists : " + orderId );
//        }
        return order;
    }

    @RequestMapping( value = "byUserName", method = RequestMethod.GET )
    public List<Order> getOrdersByUserName( @RequestParam String userName ) {
        return orderRepository.findByUsername( userName );
    }
}

