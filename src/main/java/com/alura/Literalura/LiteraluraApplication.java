package com.alura.Literalura;

import com.alura.Literalura.principal.Principal;
import com.alura.Literalura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LibrosRepository librosRepository;
	@Autowired

	public static void main(String[] args) {

		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal prinicipal = new Principal(librosRepository);
		prinicipal.muestraElMenu();
	}
}
