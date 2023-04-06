package uz.pdp.b24orderservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInventoryRequest {
    private int orderID;
    private List<OrderItemRequest> orderItemList;
}
