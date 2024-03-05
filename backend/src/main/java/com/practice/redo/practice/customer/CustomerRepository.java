package com.practice.redo.practice.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(long id);
}
