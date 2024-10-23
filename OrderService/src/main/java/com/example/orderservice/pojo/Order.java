package com.example.orderservice.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "item")
    private String item;
    @Column(name = "price")
    private Integer price;
    @Column(name = "amount")
    private Integer amount;
}
