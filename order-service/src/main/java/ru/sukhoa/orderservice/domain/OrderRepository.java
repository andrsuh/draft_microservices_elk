package ru.sukhoa.orderservice.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    @Query("select u from Order u where u.user.name=?1")
    List<Order> findByUsername(String userName);
}
