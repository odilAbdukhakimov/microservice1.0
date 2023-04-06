package uz.pdp.b24productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TestDataHelperProduct {

    private static final String PRODUCT_URL = "/product";

    @Autowired
    private MockMvc mockMvc;

    public RequestBuilder createProductRequest(String imageUrl) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Apple");
        map.put("price", 325);
        map.put("desc", "desc");
        map.put("base64ImageUrl", imageUrl);

        return post(PRODUCT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(map));
    }

    public void createProduct() throws Exception {
        mockMvc.perform(createProductRequest(getBase64()))
                .andExpect(status().isCreated());
    }

    public RequestBuilder getProductRequest(String productId) throws Exception {
        return get(PRODUCT_URL + "/" + productId);
    }


    public void getProduct(String productId) throws Exception {
        mockMvc.perform(getProductRequest(productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.price").value(325))
                .andExpect(jsonPath("$.desc").value("desc"));
    }

    private String getBase64() throws IOException {
        Resource resource = new ClassPathResource("img.png");
        File file = resource.getFile();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = inputStream.readAllBytes();
        inputStream.close();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
