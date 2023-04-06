package uz.pdp.b24productservice;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import uz.pdp.b24productservice.repository.ProductRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommonIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    protected TestDataHelperProduct testDataHelperProduct;

    @AfterEach
    void testDataCleanUp() {
        productRepository.deleteAll();
    }

    private static final String IMAGE_NAME = "postgres:latest";

    protected static final PostgreSQLContainer<?> postgres;

    static {
        postgres = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse(IMAGE_NAME))
                .withInitScript("sql/table-init.sql")
                .withExposedPorts(5432);
        postgres.withReuse(true);
        Startables.deepStart(postgres).join();
    }


    @DynamicPropertySource
    static void setUpPostgresConnectionProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.database", postgres::getDatabaseName);
    }
}
