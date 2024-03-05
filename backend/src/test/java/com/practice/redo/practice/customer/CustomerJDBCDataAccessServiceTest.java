package com.practice.redo.practice.customer;

import com.practice.redo.practice.AbstractTestContainerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainerUnitTest {

    private CustomerJDBCDataAccessService underTest;
    private  final CustomerRowMapper customerRowMapper=new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest= new CustomerJDBCDataAccessService(
                new JdbcTemplate(getDataSource()),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer= new Customer(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                43
        );
        underTest.insertCustomer(customer);
        // When

        List<Customer> customers=underTest.selectAllCustomers();

        // Then
        assertThat(customers).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        // Given
        String email = faker.internet().emailAddress();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                43
        );

        underTest.insertCustomer(customer);

        long id =underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        // When

        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then

        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){

        long id = -1;

        var actual  = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given


        // When

        // Then

    }

    @Test
    void existsPersonWithEmail() {
        // Given

        String email = faker.internet().emailAddress();
        Customer customer= new Customer(
                faker.name().fullName(),
                email,
                43
        );

        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsPersonWithEmail(email);
        // Then

        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenEmailDoNotExistTest() {
        // Given

        String email = faker.internet().emailAddress();

        // When
        boolean actual = underTest.existsPersonWithEmail(email);
        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerByID() {
        // Given

        String name= faker.name().fullName();
        String email =faker.internet().emailAddress();
        Customer customer=new Customer(name, email, 34);

        underTest.insertCustomer(customer);

        long id= underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        // When
        boolean actual=underTest.existsCustomerByID(id);

        // Then

        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByIDReturnsFalseIfNotFound() {
        // Given

        long id = -1;


        // When
        boolean actual=underTest.existsCustomerByID(id);

        // Then

        assertThat(actual).isFalse();

    }

    @Test
    void deleteCustomer() {
        // Given

        String name= faker.name().fullName();
        String email =faker.internet().emailAddress();
        Customer customer=new Customer(name, email, 34);

        underTest.insertCustomer(customer);

        long id= underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        // When

        underTest.deleteCustomer(id);

        // Then

        assertThat(underTest.existsCustomerByID(id)).isFalse();
    }


    @Test
    void updateCustomer() {
        // Given


        // When

        // Then

    }
}