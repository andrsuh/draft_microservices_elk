package ru.sukhoa.bookgatewayservice.domain;

import java.util.Set;

public class Order {
    public int id;

    public Set<OrderDetails> details;

    public Order() {
    }

    public Order( int id ) {
        this.id = id;
        this.details = null;
    }

    public Order( int id, Set<OrderDetails> details ) {
        this.id = id;
        this.details = details;
    }
}
