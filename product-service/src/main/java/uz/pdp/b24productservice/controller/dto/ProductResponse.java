package uz.pdp.b24productservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import uz.pdp.b24productservice.entity.ProductEntity;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private int id;
    private String name;
    private double price;
    private String desc;
    private String imageUrl;
    private Double quantity;

    public static ProductResponse from(ProductEntity productEntity){
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .desc(productEntity.getDescription())
                .imageUrl(productEntity.getImageUrl())
                .price(productEntity.getPrice())
                .build();
    }
}
