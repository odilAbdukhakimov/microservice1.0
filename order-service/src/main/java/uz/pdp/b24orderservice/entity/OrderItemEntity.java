package uz.pdp.b24orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.b24orderservice.controller.dto.OrderItemRequest;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int productId;
    private double amount;
    private double price;
    @ManyToOne
    private OrderEntity orderEntity;

    public static OrderItemEntity of(OrderItemRequest orderItemRequest, OrderEntity orderEntity){
        return OrderItemEntity.builder()
                .orderEntity(orderEntity)
                .amount(orderItemRequest.getQuantity())
                .price(orderItemRequest.getPrice())
                .productId(orderItemRequest.getProductId())
                .build();
    }

}
