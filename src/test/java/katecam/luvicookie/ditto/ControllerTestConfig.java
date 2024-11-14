package katecam.luvicookie.ditto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@Sql(value = "classpath:test_db/data.sql", config = @SqlConfig(encoding = "UTF-8"))
@SpringBootTest
public abstract class ControllerTestConfig {

    protected WebApplicationContext context;
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;

    @Autowired
    public ControllerTestConfig(WebApplicationContext context, ObjectMapper objectMapper) {
        this.context = context;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
