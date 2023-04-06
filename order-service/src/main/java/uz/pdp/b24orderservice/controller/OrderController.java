package uz.pdp.b24orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.pdp.b24orderservice.controller.dto.OrderRequest;
import uz.pdp.b24orderservice.controller.dto.OrderResponse;
import uz.pdp.b24orderservice.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid @RequestBody OrderRequest orderRequest
    ) {
        return orderService.createOrder(orderRequest);
    }

//    @GetMapping("/{orderId}")
//    public OrderResponse getSingleOrder(
//            @PathVariable int orderId
//    ) {
//        OrderEntity orderEntity = orderService.getOrderById(orderId);
//        return OrderResponse.from(orderEntity);
//    }

    @GetMapping()
    public List<OrderResponse> getOrderList(
            @RequestParam(name = "username") String username
    ) {
        return orderService.getOrderList(username);
    }

    @GetMapping("/check/{orderId}")
    public void getOrderStatus(
            @PathVariable int orderId
    ){
        orderService.getOrderStatus(orderId);
    }

}
