package com.assisto.virtualassistant;

import com.assisto.virtualassistant.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class AsistoApplication implements CommandLineRunner {
	@Autowired
	private TerminalMenu terminalMenu;

	public AsistoApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(AsistoApplication.class, args);
	}
	//VIN Audi: WAUZZZ8K1EN029439
	//VIN VW: WVWZZZ1KZCM714681
	public void run(String... args) throws Exception {
		terminalMenu.showMenu();
	}
}