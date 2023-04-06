package uz.pdp.inventoryservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryRequest extends InventoryResponse{
    InventoryRequest(int productId, double quantity) {
        super(productId, quantity);
    }
}
