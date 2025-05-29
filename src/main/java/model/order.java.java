package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

    @CreationTimestamp
    private LocalDateTime createdAt;
}