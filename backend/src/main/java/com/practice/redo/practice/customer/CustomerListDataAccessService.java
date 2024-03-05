package com.practice.redo.practice.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    static List<Customer> customers;

    static {
        customers=new ArrayList<>();
        customers.add(new Customer("Alex","Peterson@hotmail.com",28));
        customers.add(new Customer("Joe","joe@gmail.com",24));
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(long id) {
        return customers.stream().filter(c -> c.getId()==id).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existsCustomerByID(long id) {
        return false;
    }

    @Override
    public void deleteCustomer(long id) {

    }

    @Override
    public void updateCustomer(Customer update) {

    }
}
