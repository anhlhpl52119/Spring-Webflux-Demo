package northwind.controller;

import northwind.model.Customer;
import northwind.service.api.ICustomerService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @RequestMapping(value = "/customer-stream", method = RequestMethod.GET )
    public Mono<Customer> getAllCustomer(){
        return null;
    }

    @RequestMapping( value = "/customer/{customerId}",method = RequestMethod.GET)
    public Mono<Customer> getCustomer(@PathVariable("customerId") String customerId){
        return customerService.getCustomer(customerId);
    }
}
