package com.mandacarubroker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application class for Mandacarubroker.
 * This class initializes and runs the Mandacarubroker application using Spring Boot.
 */
@SpringBootApplication
public class MandacarubrokerApplication {

	/**
	 * The main method of the Mandacarubroker application.
	 * This method initializes and starts the Spring Boot application.
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(final String[] args) {
		SpringApplication.run(MandacarubrokerApplication.class, args);
	}

}
