package uz.pdp.b24orderservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotBlank
    private int productId;
    @NotNull
    private double quantity;

    private double price;
}
