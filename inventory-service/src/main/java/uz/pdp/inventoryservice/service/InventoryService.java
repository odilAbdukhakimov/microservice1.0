package uz.pdp.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import uz.pdp.inventoryservice.common.exception.AlreadyExistException;
import uz.pdp.inventoryservice.common.exception.ProductAmountException;
import uz.pdp.inventoryservice.common.exception.ProductNotFoundException;
import uz.pdp.inventoryservice.common.exception.RecordNotFoundException;
import uz.pdp.inventoryservice.controller.dto.*;
import uz.pdp.inventoryservice.entity.InventoryEntity;
import uz.pdp.inventoryservice.repository.InventoryRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String PRODUCT_URL = "http://PRODUCT-SERVICE/product/";

    public InventoryEntity create(InventoryRequest inventoryRequest) {
        int productId = inventoryRequest.getProductId();
        if (!isExistProduct(productId)) {
            throw new RecordNotFoundException(String.format("product not found in product service for %s", productId));
        }
        inventoryRepository.findByProductId(productId).ifPresent(product -> {
            throw new AlreadyExistException(
                    MessageFormat.format("{0} product already exist", productId)
            );
        });
        InventoryEntity inventoryEntity = InventoryEntity.from(inventoryRequest);
        return inventoryRepository.save(inventoryEntity);
    }

    private boolean isExistProduct(int productId) {
        return restTemplate.getForObject(PRODUCT_URL + productId, ProductResponse.class) != null;
    }

    public InventoryEntity getByProductId(int id, double quantity) {
        InventoryEntity inventoryEntity = inventoryRepository.findByProductId(id).orElseThrow(() ->
                new RecordNotFoundException(String.format(" %d id, product not found", id)));
        if (inventoryEntity.getQuantity() < quantity) {
            throw new IllegalStateException(String.format("Sorry, we have %s product, but you requested %s products to buy", inventoryEntity.getQuantity(), quantity));
        }
        return inventoryEntity;
    }

    public InventoryEntity updateInventory(InventoryUpdateRequest inventoryUpdateRequest) {
        int productId = inventoryUpdateRequest.getProductId();
        if (!isExistProduct(productId)) {
             throw new  ProductNotFoundException("product not found");
        }
        InventoryEntity inventoryEntity = inventoryRepository.getByProductId(productId);
        inventoryEntity.setQuantity(inventoryEntity.getQuantity() + inventoryUpdateRequest.getQuantity());
        return inventoryRepository.save(inventoryEntity);

    }


    public InventoryEntity getByProductIdFromInventory(int productId) {
        return inventoryRepository.getByProductId(productId);
    }

    public List<InventoryResponse> getByProductIds(List<Integer> productIds) {
        return inventoryRepository.findAllByProductIdIn(productIds).stream().map(InventoryResponse::from).toList();
    }

    @Transactional(rollbackFor = {ProductAmountException.class, ProductNotFoundException.class})
    public Boolean check(OrderInventoryRequest orderInventoryRequest) {
        orderInventoryRequest.getOrderItemList().forEach(order-> {
            InventoryEntity inventoryEntity = inventoryRepository.findByProductId(order.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(String.format("product not found for %s", order.getProductId())));

            if (inventoryEntity.getQuantity() < order.getQuantity()) {
                throw new ProductAmountException(String.format("product quantity not enough for %s", order.getProductId()));
            }
            inventoryEntity.setQuantity(inventoryEntity.getQuantity() - order.getQuantity());
            inventoryRepository.save(inventoryEntity);
        });
        kafkaTemplate.send("inventory_service", String.valueOf(orderInventoryRequest.getOrderID()));
        return true;
    }
}
