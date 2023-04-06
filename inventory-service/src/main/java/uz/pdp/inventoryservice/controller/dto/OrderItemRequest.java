package uz.pdp.inventoryservice.controller.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private int productId;

    private double quantity;

    private double price;
}
