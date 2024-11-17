package com.mony.payment.integration;

import com.mony.payment.model.dtos.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name="order")
public interface OrderFeignClient {
    @GetMapping("/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable UUID orderId);

    @PutMapping("/orders/{orderId}")
    OrderDTO updateOrderById(@PathVariable("orderId") UUID orderId, @RequestBody OrderDTO orderDTO);
}
