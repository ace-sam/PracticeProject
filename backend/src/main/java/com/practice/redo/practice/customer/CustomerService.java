package com.practice.redo.practice.customer;

import com.practice.redo.practice.exception.DuplicateResourceException;
import com.practice.redo.practice.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomerById(long id){
        return customerDAO.selectCustomerById(id)
                .orElseThrow(()-> new ResourceNotFound("Customer with id [%s] not found".formatted(id)));
    }

    void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){

        //
        String email = customerRegistrationRequest.email();
        if (customerDAO.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }
        // add
        customerDAO.insertCustomer(
                new Customer(customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age())
        );
    }

    public void deleteCustomerById(long id){
        if (!customerDAO.existsCustomerByID(id)){
            throw new ResourceNotFound("customer with id [%s] not found".formatted(id));
        }

        customerDAO.deleteCustomer(id);
    }

    public void updateCustomer(long CustomerId, CustomerUpdateRequest updateRequest){

        if(updateRequest.email() != null && customerDAO.existsPersonWithEmail(updateRequest.email())){
            throw new DuplicateResourceException("email already exists");
        }

        Customer customer=customerDAO.selectCustomerById(CustomerId).stream().toList().get(0);

//        System.out.println("*********************************");
//        System.out.println(customer.getEmail());
//        System.out.println(updateRequest.email());
//        System.out.println(customer.getId());

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            customer.setEmail(updateRequest.email());

        }else customer.setEmail(null);

        if(updateRequest.age() != 0 && updateRequest.age() != customer.getAge()){
            customer.setAge(updateRequest.age());
        }

        customerDAO.updateCustomer(customer);
    }


}
