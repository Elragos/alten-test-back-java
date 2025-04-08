package fr.alten.test_back;

import fr.alten.test_back.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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
    private final UserController controller;

    public TestBackApplicationTests(UserController controller){
        this.controller = controller;
    }

    /**
     * Test if context loads correctly.
     */
    @Test
    public void contextLoads() {
        Assertions.assertThat(this.controller).isNotNull();
    }

}
