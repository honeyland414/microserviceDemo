package com.example.orderservice.controller;


import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.OrderResponse;
import com.example.orderservice.pojo.User;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final UserClient userClient;
//    private final RestTemplate restTemplate;
    @Autowired
    public OrderController(OrderService orderService, UserClient userClient) {
        this.orderService = orderService;
        this.userClient = userClient;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Integer id) {

        Order order = orderService.getOrderById(id).orElse(null);
        User user = userClient.getUserById(order.getUserId());

        return OrderResponse.builder()
                .order(order)
                .user(user)
                .build();
    }
}
