package com.example.onlineshop_backend.dto;

import lombok.Data;

@Data
public class CartItemDTO {

    private Long id;

    private Integer price;

    private Long quantity;

    private Long productId;

    private Long orderId;

    private String productName;

    private byte[] returnedImg;

    private Long userId;
}
