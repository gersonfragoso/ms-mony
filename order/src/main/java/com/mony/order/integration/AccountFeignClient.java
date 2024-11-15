package com.mony.order.integration;

import com.mony.order.dto.LoginRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="account")
public interface AccountFeignClient {
    @PostMapping("/api/users/login")
    ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO);
}
