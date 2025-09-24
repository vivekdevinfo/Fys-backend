package com.khoahd7621.youngblack.repositories;

import com.khoahd7621.youngblack.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Optional<Order> findByCode(String code);

    public List<Order> findAllByUserId(Long userId);
}
