package uz.pdp.b24orderservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.b24orderservice.entity.OrderItemEntity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemResponse {
    private int id;
    private int productId;
    private double price;
    private double amount;
    public static OrderItemResponse from(OrderItemEntity orderItemEntity){
        return OrderItemResponse.builder()
                .id(orderItemEntity.getId())
                .amount(orderItemEntity.getAmount())
                .price(orderItemEntity.getPrice())
                .productId(orderItemEntity.getProductId())
                .build();
    }
}
