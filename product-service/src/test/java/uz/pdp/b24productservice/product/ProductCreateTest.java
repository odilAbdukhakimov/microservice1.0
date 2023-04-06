package uz.pdp.b24productservice.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import uz.pdp.b24productservice.CommonIntegrationTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("create a new product POST( /product )")
public class ProductCreateTest extends CommonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("should create a new product")
    public void shouldCreateProduct() throws Exception {
        testDataHelperProduct.createProduct();
    }

    @Test
    @DisplayName("can get product by id")
    public void shouldGetProduct() throws Exception{
        testDataHelperProduct.createProduct();
        testDataHelperProduct.getProduct("1");
    }

    @Test
    @DisplayName("should throw when get product by id")
    public void shouldThrowProductWhenProductDoesntExist() throws Exception{
        RequestBuilder productRequest = testDataHelperProduct.getProductRequest("1");
        mockMvc.perform(productRequest).andExpect(status().isNotFound());
    }

}
