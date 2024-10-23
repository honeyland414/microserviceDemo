package com.example.orderservice.controller;


import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.OrderResponse;
import com.example.orderservice.pojo.User;
import com.example.orderservice.service.OrderService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderController(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Integer id) {
        Tracer tracer = GlobalOpenTelemetry.get().getTracer("orderservice", "1.0");

        String url = "http://userService/user/";
        String url1 = "http://localhost:8081/user/";
        Order order;
        User user;

        Span span = tracer.spanBuilder("order.service.controller").setSpanKind(SpanKind.CLIENT).startSpan();
        try (Scope scope = span.makeCurrent()) {
            order = orderService.getOrderById(id).orElse(null);
            span.addEvent("orderservice", Attributes.of(AttributeKey.stringKey("data"), order.toString()));

            user = restTemplate.getForObject(url1 + order.getUserId(), User.class);

            span.setStatus(StatusCode.OK);
        } finally {
            span.end();
        }


        return OrderResponse.builder()
                .order(order)
                .user(user)
                .build();
    }
}
