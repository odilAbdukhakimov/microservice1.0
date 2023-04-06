package uz.pdp.b24orderservice.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String username;
    private List<OrderItemRequest> orderItemList;

}
