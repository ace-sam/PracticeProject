package com.practice.redo.practice.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> selectAllCustomers();


    Optional<Customer> selectCustomerById(long id);

    void insertCustomer(Customer customer);

    boolean existsPersonWithEmail(String email);
    boolean existsCustomerByID(long id);
    void deleteCustomer(long id);

    void updateCustomer(Customer update);

}
