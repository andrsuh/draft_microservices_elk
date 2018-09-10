package ru.sukhoa.bookgatewayservice.domain;

public class OrderDetails {

    public final int orderId;

    public final int bookId;

    public final int count;

    public OrderDetails(int orderId, int bookId, int count ) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.count = count;
    }
}