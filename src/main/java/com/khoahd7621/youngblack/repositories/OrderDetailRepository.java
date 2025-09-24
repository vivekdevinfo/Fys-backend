package com.khoahd7621.youngblack.repositories;

import com.khoahd7621.youngblack.entities.OrderDetail;
import com.khoahd7621.youngblack.entities.composite.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
}
