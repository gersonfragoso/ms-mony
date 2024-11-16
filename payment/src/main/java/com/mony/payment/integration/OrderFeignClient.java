package com.mony.payment.integration;

import com.mony.payment.model.dtos.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="order")
public interface OrderFeignClient {
    @GetMapping("/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable Long orderId);
    //OrderDTO getOrderById(@PathVariable UUID orderId);
}
