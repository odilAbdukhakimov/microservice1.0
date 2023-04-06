package uz.pdp.inventoryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import uz.pdp.inventoryservice.controller.dto.InventoryRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int productId;
    private double quantity;
    public static InventoryEntity from(InventoryRequest inventoryRequest) {
        return InventoryEntity.builder()
                .productId(inventoryRequest.getProductId())
                .quantity(inventoryRequest.getQuantity())
                .build();
    }

}
