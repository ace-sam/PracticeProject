package com.practice.redo.practice.customer;

import com.practice.redo.practice.exception.DuplicateResourceException;
import com.practice.redo.practice.exception.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest=new CustomerService(customerDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {
        // Given
        underTest.getAllCustomers();
        // When

        // Then
        verify(customerDAO).selectAllCustomers();

    }

    @Test
    void getCustomerByIdFound() {
        // Given
        long id=10;
        Customer customer=new Customer(id,"Alex","alex@gmail.con",25);

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomerById(id);

        // Then
        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void getCustomerByIdUnFound() {
        // Given
        long id=10;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(()-> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        // Given

        String email = "alex@email.cin";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest("sam",email,50);

        // When
        underTest.addCustomer(customerRegistrationRequest);

        // Then

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(0L);
        assertThat(capturedCustomer.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRegistrationRequest.age());
    }

    @Test
    void addCustomerFails() {
        // Given

        String email = "alex@email.cin";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest customerRegistrationRequest =
                new CustomerRegistrationRequest("sam",email,50);

        //When
        assertThatThrownBy(()->underTest.addCustomer(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given

        long id =1;

        when(customerDAO.existsCustomerByID(id)).thenReturn(true);


        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDAO).deleteCustomer(id);
    }

    @Test
    void deleteCustomerByIdFails() {
        // Given

        long id =1;

        when(customerDAO.existsCustomerByID(id)).thenReturn(false);

        assertThatThrownBy(()->underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        verify(customerDAO, never()).deleteCustomer(id);
    }

    @Test
    void updateCustomerAllData() {
        // Given

        long id=10;

        Customer c=new Customer(id,"Toti","pop",40);


        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(
                c
        ));

        String newEmail = "alex@email.cin";

        CustomerUpdateRequest request =
                new CustomerUpdateRequest("sam",newEmail,50);


        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id,request);

        // Then

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }


    @Test
    void updateCustomerNameOnly() {
        // Given

        long id=10;

        //Original Customer in DB
        Customer c=new Customer(id,"Toti","pop@pop.com",40);

        //Making selectCustomerById returns our customer above
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(c));

        //making an edit of name only
        CustomerUpdateRequest request =
                new CustomerUpdateRequest("Sam",null,0);


        // When
        underTest.updateCustomer(id, request);

        // Then

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(c.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(c.getAge());

    }

    @Test
    void updateCustomerAgeOnly() {
        // Given

        long id=10;

        //Original Customer in DB
        Customer c=new Customer(id,"Toti","pop@pop.com",40);

        //Making selectCustomerById returns our customer above
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(c));

        //making an edit of name only
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null,null,41);


        // When
        underTest.updateCustomer(id, request);

        // Then

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(c.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(c.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void updateCustomerEmailOnly() {
        // Given

        long id=10;

        //Original Customer in DB
        Customer c=new Customer(id,"Toti","pop@pop.com",40);

        //Making selectCustomerById returns our customer above
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(c));

        //making an edit of name only
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null,"toi@gmail.com",0);


        when(customerDAO.existsPersonWithEmail("toi@gmail.com")).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then

        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(c.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(c.getAge());

    }

    @Test
    void updateCustomerThrowsErrorWhenEmailAlreadyTaken() {
        // Given

        long id=10;

        //Original Customer in DB
        Customer c=new Customer(id,"Toti","pop@pop.com",40);

        //making an edit of name only
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null,"pop@pop.com",0);


        when(customerDAO.existsPersonWithEmail("pop@pop.com")).thenReturn(true);

        // When
        assertThatThrownBy(()->
                underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists");

        // Then
        verify(customerDAO, never()).updateCustomer(any());


    }

}