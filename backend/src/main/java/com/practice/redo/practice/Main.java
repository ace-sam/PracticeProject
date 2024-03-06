package com.practice.redo.practice;

import com.github.javafaker.Faker;
import com.practice.redo.practice.customer.Customer;
import com.practice.redo.practice.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {


	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}


	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository){
		return args -> {

			Faker faker = new Faker();

			String name = faker.name().fullName(); // Miss Samanta Schmidt
			String email = faker.internet().emailAddress(); // Emory
			int age= new Random().nextInt(16,99);

			Customer lista= new Customer(name,email,age);
			customerRepository.save(lista);
		};
	}

}
