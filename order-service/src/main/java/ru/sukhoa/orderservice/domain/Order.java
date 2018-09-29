package ru.sukhoa.orderservice.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private int id = (int) (Math.random() * 100_000);

    private int userId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.COLLECTING;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "orderId")
    public Set<OrderDetails> details;

    public Order() {
    }

    public Order(int userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public boolean isCollecting() {
        return this.status == OrderStatus.COLLECTING;
    }
}
