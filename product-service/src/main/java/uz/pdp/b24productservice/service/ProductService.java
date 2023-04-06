package uz.pdp.b24productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.pdp.b24productservice.common.exception.RecordNotFoundException;
import uz.pdp.b24productservice.controller.dto.InventoryResponse;
import uz.pdp.b24productservice.controller.dto.ProductRequest;
import uz.pdp.b24productservice.controller.dto.ProductResponse;
import uz.pdp.b24productservice.entity.ProductEntity;
import uz.pdp.b24productservice.repository.ProductRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String IMAGE_URL = "src/main/resources/static/images/";
    private static final String ALL_AVAILABLE_PRODUCTS_URL = "http://INVENTORY-SERVICE/inventory/list";
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    public ProductEntity create(
            ProductRequest productRequest
    ) {
        ProductEntity productEntity = ProductEntity.builder()
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .description(productRequest.getDesc())
//                .imageUrl(saveImage(productRequest.getBase64ImageUrl()))
                .build();

        return productRepository.save(productEntity);
    }

    public ProductEntity get(Integer id) {
        return productRepository.findById(id).orElseThrow(
                () ->
                        new RecordNotFoundException(MessageFormat.format("product {0} not found", id))
        );
    }

    @SneakyThrows
    private String saveImage(String base64) {
        byte[] decode = Base64.getDecoder().decode(base64);
        String imageUrl = IMAGE_URL + getFileName() + ".png";
        File newFile = new File(imageUrl);
        newFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(newFile);
        outputStream.write(decode);
        outputStream.close();

        return imageUrl;
    }

    private String getFileName() {
        return UUID.randomUUID().toString();
    }

    public List<ProductResponse> getProductsByPagination(int page, int size, String sort) {
        try {
            PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));
            List<ProductEntity> productEntityList = productRepository.findAll(pageable).toList();
            List<Integer> ids = productEntityList.stream().map(ProductEntity::getId).toList();
            HttpEntity<?> requestBody = new HttpEntity<>(Map.of("productIds", ids));
            ResponseEntity<InventoryResponse[]> responseEntity = restTemplate.exchange(ALL_AVAILABLE_PRODUCTS_URL, HttpMethod.POST, requestBody, InventoryResponse[].class);
            InventoryResponse[] inventoryList = responseEntity.getBody();
            return convert(productEntityList, inventoryList);
        } catch (Exception e) {
            return null;
        }
    }

    private List<ProductResponse> convert(List<ProductEntity> productEntityList, InventoryResponse[] inventoryList) {

        return productEntityList.stream().map((productEntity -> {
            Double productAmount = getProductAmount(productEntity, inventoryList);
            return ProductResponse.builder()
                    .id(productEntity.getId())
                    .name(productEntity.getName())
                    .price(productEntity.getPrice())
                    .desc(productEntity.getDescription())
                    .imageUrl(productEntity.getImageUrl())
                    .quantity(productAmount == null ? 0 : productAmount)
                    .build();
        }
        )).toList();
    }

    private Double getProductAmount(ProductEntity productEntity, InventoryResponse[] inventoryList) {
        for (InventoryResponse inventory : inventoryList) {
            if (productEntity.getId() == inventory.getProductId()) {
                return inventory.getQuantity();
            }
        }
        return null;
    }
}
