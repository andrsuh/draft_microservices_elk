package ru.sukhoa.orderservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    private int id = ( int ) ( Math.random() * 100_000 );

    private int orderId;

    private int bookId;

    private int count;

    public OrderDetails() {
    }

    public OrderDetails( int orderId, int bookId, int count ) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.count = count;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getCount() {
        return count;
    }
}