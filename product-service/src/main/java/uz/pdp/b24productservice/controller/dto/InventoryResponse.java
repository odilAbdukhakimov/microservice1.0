package uz.pdp.b24productservice.controller.dto;

import lombok.Data;

@Data
public class InventoryResponse {
    private int productId;
    private double quantity;
}
