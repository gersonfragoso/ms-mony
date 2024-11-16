package com.mony.order.repository;

import com.mony.order.model.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, UUID> {
}
