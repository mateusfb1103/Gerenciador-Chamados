package com.example.chamado;

import org.springframework.boot.SpringApplication;

public class TestChamadoApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChamadoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
