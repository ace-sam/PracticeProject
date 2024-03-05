package com.practice.redo.practice.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable() long id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable long customerId){
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void editCustomer(@PathVariable("customerId") long customerId,
                             @RequestBody CustomerUpdateRequest updateRequest){
        customerService.updateCustomer(customerId, updateRequest);
    }








//    @GetMapping("/greet")
//    public GreetResponse greet(@RequestParam(value = "name",required = false) String name){
//        String greetMessage= name==null || name.isBlank()?"Hello": "Hello "+ name;
//        GreetResponse greetResponse=new GreetResponse(
//                greetMessage,
//                List.of("java","GoLang","C"),
//                new Person("Alex",39,30_000));
//        return greetResponse;
//    }
//
//    record GreetResponse(String message, List list,Person person){};
//
//    record Person(String name, int age, long savings){};
//
//    @GetMapping("/console/{name}")
//    public Person getPerson(@PathVariable(value = "julia") String name){
//        return new Person(name,29,3999);
//    }
}
