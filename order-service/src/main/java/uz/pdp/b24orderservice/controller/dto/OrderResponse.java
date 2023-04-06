package uz.pdp.b24orderservice.controller.dto;

import lombok.Builder;
import lombok.Data;
import uz.pdp.b24orderservice.entity.OrderEntity;
import uz.pdp.b24orderservice.entity.OrderItemEntity;
import uz.pdp.b24orderservice.entity.OrderStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
public class OrderResponse {
    private int id;
    private List<OrderItemResponse> orderItemResponseList;
    private String username;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private OrderStatus status;
    private double amount;

    public static OrderResponse from(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntityList) {
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        List<OrderItemResponse> orderItemResponses = orderItemEntityList.stream().map(item -> {
            sum.updateAndGet(v -> v + (item.getAmount() * item.getPrice()));
            return OrderItemResponse.from(item);
        }).toList();

        return OrderResponse.builder()
                .id(orderEntity.getId())
                .orderItemResponseList(orderItemResponses)
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .amount(sum.get())
                .status(orderEntity.getStatus())
                .username(orderEntity.getUsername())
                .build();
    }
}
