package fr.alten.test_back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TestBackApplicationTests {

	@Test
	void contextLoads() {
	}

}
