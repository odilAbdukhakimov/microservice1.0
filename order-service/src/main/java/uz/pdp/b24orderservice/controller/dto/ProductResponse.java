package uz.pdp.b24orderservice.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private int id;
    private String name;
    private double price;
    private String desc;
    private String imageUrl;

}
