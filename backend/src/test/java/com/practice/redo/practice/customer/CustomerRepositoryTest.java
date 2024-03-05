package com.practice.redo.practice.customer;

import com.practice.redo.practice.AbstractTestContainerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainerUnitTest {

    @Autowired
    private CustomerRepository underTest;

//    public CustomerRepositoryTest(CustomerRepository underTest) {
//        this.underTest = underTest;
//    }

    @BeforeEach
    void setUp() {
        System.out.println("hehe");
    }

    @Test
    void existsCustomerByEmail() {
        // Given

        String name= faker.name().fullName();
        String email =faker.internet().emailAddress();
        Customer customer=new Customer(name, email, 34);

        underTest.save(customer);

        // When
        boolean actual= underTest.existsCustomerByEmail(email);
        // Then

        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByEmailFailWhenEmailNotPresent() {
        // Given
        String email =faker.internet().emailAddress();

        // When
        boolean actual= underTest.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given

        String name= faker.name().fullName();
        String email =faker.internet().emailAddress();
        Customer customer=new Customer(name, email, 34);

        underTest.save(customer);

        long id = underTest.findAll().stream().filter( c ->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        // When

        // Then

        assertThat(underTest.existsCustomerById(id)).isTrue();

    }

    @Test
    void existsCustomerByIdNonFoundTest() {
        // Given

        long id=-1;

        // When

        // Then
        assertThat(underTest.existsCustomerById(id)).isFalse();
    }
}