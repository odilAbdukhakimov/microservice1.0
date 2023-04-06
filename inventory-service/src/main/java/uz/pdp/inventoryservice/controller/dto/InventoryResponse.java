package uz.pdp.inventoryservice.controller.dto;

import lombok.*;
import uz.pdp.inventoryservice.entity.InventoryEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private int productId;
    private double quantity;
    public static InventoryResponse from(InventoryEntity inventoryEntity){
        return InventoryResponse.builder()
                .productId(inventoryEntity.getProductId())
                .quantity(inventoryEntity.getQuantity())
                .build();
    }
}
