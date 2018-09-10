package ru.sukhoa.orderservice.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table( name = "orders" )
public class Order {
    @Id
    private int id = ( int ) ( Math.random() * 100_000 );

    @OneToOne( fetch = FetchType.EAGER )
    private User user;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "orderId" )
    public Set<OrderDetails> details;

    public Order() {
    }

    public Order( User user ) {
        this.user = user;
    }

    public long getId() {
        return id;
    }
}
