package com.conferencias.tfg;

import com.braintreegateway.BraintreeGateway;
import com.conferencias.tfg.utilities.BraintreeGatewayFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories
public class GestionConferenciasApp {

	public static String DEFAULT_CONFIG_FILENAME = "config.properties";
	public static BraintreeGateway gateway;

	public static void main(String[] args) {
		File configFile = new File(DEFAULT_CONFIG_FILENAME);
		try {
			if(configFile.exists() && !configFile.isDirectory()) {
				gateway = BraintreeGatewayFactory.fromConfigFile(configFile);
			} else {
				gateway = BraintreeGatewayFactory.fromConfigMapping(System.getenv());
			}
		} catch (NullPointerException e) {
			System.err.println("Could not load Braintree configuration from config file or system environment.");
			System.exit(1);
		}

		SpringApplication.run(GestionConferenciasApp.class, args);
	}

}
