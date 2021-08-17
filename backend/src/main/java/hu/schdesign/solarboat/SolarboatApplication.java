package hu.schdesign.solarboat;

import hu.schdesign.solarboat.dao.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SolarboatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolarboatApplication.class, args);
	}

}
