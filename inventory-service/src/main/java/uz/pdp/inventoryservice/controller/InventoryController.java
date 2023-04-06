package uz.pdp.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.pdp.inventoryservice.controller.dto.*;
import uz.pdp.inventoryservice.entity.InventoryEntity;
import uz.pdp.inventoryservice.service.InventoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse create(
            @RequestBody InventoryRequest inventoryRequest
    ) {

        InventoryEntity inventoryEntity = inventoryService.create(inventoryRequest);
        return InventoryResponse.from(inventoryEntity);
    }

    @GetMapping("/{productId}/{quantity}")
    public InventoryResponse getByProductId(
            @PathVariable int productId,
            @PathVariable double quantity
    ) {
        InventoryEntity byProductId = inventoryService.getByProductId(productId,quantity);
        return InventoryResponse.from(byProductId);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getProductFromInventory(
            @PathVariable int productId
    ) {
        InventoryEntity byProductId = inventoryService.getByProductIdFromInventory(productId);
        return InventoryResponse.from(byProductId);
    }

    @PutMapping()
    public InventoryResponse updateInventory(
           @RequestBody InventoryUpdateRequest inventoryUpdateRequest
    ){
        InventoryEntity inventoryEntity = inventoryService.updateInventory(inventoryUpdateRequest);
        return InventoryResponse.from(inventoryEntity);
    }

    @PostMapping("/list")
    public List<InventoryResponse> getListOfPageableProducts(@RequestBody Map<String, List<Integer>> productIds){
        return inventoryService.getByProductIds(productIds.get("productIds"));
    }

    @PostMapping("/check")
    public Boolean check(@RequestBody OrderInventoryRequest orderInventoryRequest){
        return inventoryService.check(orderInventoryRequest);
    }
}
