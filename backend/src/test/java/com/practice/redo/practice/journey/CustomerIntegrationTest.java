package com.practice.redo.practice.journey;

import com.github.javafaker.Faker;
import com.practice.redo.practice.customer.Customer;
import com.practice.redo.practice.customer.CustomerRegistrationRequest;
import com.practice.redo.practice.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random random=new Random();
    String customerUri = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        // create a registration request

        Faker faker= new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name,email,age
        );


        // send a post request

        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers

        List<Customer> allCustomers= webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // male sure that customer is present

        Customer expectedCustomer= new Customer(name, email, age);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        long id = allCustomers.stream()
                .filter( customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        // get customer by id
        webTestClient.get()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);


    }



    @Test
    void canDeleteACustomer() {
        // create a registration request

        Faker faker= new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name,email,age
        );


        // send a post request

        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers

        List<Customer> allCustomers= webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get id

        long id = allCustomers.stream()
                .filter( customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //delete customer

        webTestClient.delete()
                .uri(customerUri+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(customerUri+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdateCustomer() {
        // create a registration request

        Faker faker= new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest(
                name,email,age
        );


        // send a post request

        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers

        List<Customer> allCustomers= webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        long id = allCustomers.stream()
                .filter( customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer

        String newName=faker.name().fullName();
        String newEmail=faker.internet().emailAddress();


        CustomerUpdateRequest updateRequest=new CustomerUpdateRequest(
                newName, newEmail,21
        );


        webTestClient.put()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();




        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();


        Customer newCustomer= new Customer(id, newName, newEmail, 21);

        assertThat(updatedCustomer).isEqualTo(newCustomer);

    }
}
