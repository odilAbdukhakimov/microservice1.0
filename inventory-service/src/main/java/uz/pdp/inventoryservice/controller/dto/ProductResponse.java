package uz.pdp.inventoryservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private int id;
    private String name;
    private double price;
    private String desc;
    private String imageUrl;

}
