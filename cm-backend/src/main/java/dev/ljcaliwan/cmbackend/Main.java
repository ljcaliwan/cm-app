package dev.ljcaliwan.cmbackend;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dev.ljcaliwan.cmbackend.customer.Customer;
import dev.ljcaliwan.cmbackend.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			Faker faker = new Faker();
			Random random = new Random();
			var firstName = faker.name().firstName();
			var lastname = faker.name().lastName();
			Customer customer = new Customer(
					firstName + " " + lastname,
					firstName.toLowerCase() + "." + lastname.toLowerCase() + "@gmail.com",
					random.nextInt(18, 99)
			);
			//customerRepository.save(customer);
		};
	}
}
