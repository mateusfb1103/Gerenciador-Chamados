package com.example.chamado;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ChamadoApplicationTests {

	@Test
	void contextLoads() {
	}

}
