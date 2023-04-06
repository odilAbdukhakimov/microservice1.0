package uz.pdp.inventoryservice.controller.dto;

import lombok.Data;

@Data
public class InventoryUpdateRequest {
    private InventoryEnums inventoryEnums;
    private int productId;
    private double quantity;

}
