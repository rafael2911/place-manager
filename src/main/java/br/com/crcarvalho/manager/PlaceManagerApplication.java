package br.com.crcarvalho.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class PlaceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceManagerApplication.class, args);
	}

}
