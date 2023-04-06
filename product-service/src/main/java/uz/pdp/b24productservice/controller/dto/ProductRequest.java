package uz.pdp.b24productservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String desc;
    private String base64ImageUrl;
    @NotNull
    private double price;
}
