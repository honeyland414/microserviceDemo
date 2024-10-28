package com.example.userservice.controller;


import com.example.userservice.pojo.User;
import com.example.userservice.service.UserService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        System.out.println(userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        Tracer tracer = GlobalOpenTelemetry.get().tracerBuilder("userservice").build();
        Span span = tracer.spanBuilder("order.service.controller").setSpanKind(SpanKind.CLIENT).startSpan();
        logger.info("userservice.tracer", tracer);
        logger.info("userservice.span", span);
        try (Scope scope = span.makeCurrent()) {
            Optional<User> user = userService.getUserById(id);

            span.setStatus(StatusCode.OK);
            return user.orElse(null);
        } finally {
            span.end();
        }
    }
}
