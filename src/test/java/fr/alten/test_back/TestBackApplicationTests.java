package fr.alten.test_back;

import fr.alten.test_back.controller.LoginController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Global application tests.
 *
 * @author amarechal
 */
@Import(TestContainersConfiguration.class)
@SpringBootTest
public class TestBackApplicationTests {

    /**
     * Controller used to test if context loads correctly.
     */
    @Autowired
    private LoginController controller;

    /**
     * Test if context loads correctly.
     */
    @Test
    public void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }

}
