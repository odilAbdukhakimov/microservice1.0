package uz.pdp.b24productservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.pdp.b24productservice.controller.dto.ProductRequest;
import uz.pdp.b24productservice.controller.dto.ProductResponse;
import uz.pdp.b24productservice.entity.ProductEntity;
import uz.pdp.b24productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        ProductEntity productEntity = productService.create(productRequest);
        return ProductResponse.from(productEntity);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse get(
            @PathVariable Integer id
    ) {
        ProductEntity productEntity = productService.get(id);
        return ProductResponse.from(productEntity);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sort){
        return productService.getProductsByPagination(page, size, sort);
    }
}
