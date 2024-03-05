package com.practice.redo.practice.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService undertest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable= MockitoAnnotations.openMocks(this);
        undertest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // Given
        undertest.selectAllCustomers();

        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        undertest.selectCustomerById(1);
        // When

        // Then
        verify(customerRepository).findById(1l);

    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer=new Customer(2,"sami","sami@yahoo.com",25);
        // When
        undertest.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        Customer customer=new Customer(2,"sami","sami@yahoo.com",25);
        // When
        undertest.existsPersonWithEmail("sami@yahoo.com");
        // Then
        verify(customerRepository).existsCustomerByEmail("sami@yahoo.com");
    }

    @Test
    void existsCustomerByID() {
        // Given
        Customer customer=new Customer(2,"sami","sami@yahoo.com",25);
        // When
        undertest.existsCustomerByID(2);
        // Then
        verify(customerRepository).existsCustomerById(2);

    }

    @Test
    void deleteCustomer() {
        // Given

        // When
        undertest.deleteCustomer(2);
        // Then
        verify(customerRepository).deleteById(2l);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer=new Customer(2,"sami","sami@yahoo.com",25);
        // When
        undertest.updateCustomer(customer);
        // Then
        verify(customerRepository).save(customer);

    }
}