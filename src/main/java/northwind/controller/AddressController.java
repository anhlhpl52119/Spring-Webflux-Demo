package northwind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import northwind.model.Customer;
import northwind.service.api.ICustomerService;
import reactor.core.publisher.Mono;

@RestController
public class AddressController {

    @Autowired
    ICustomerService customerService;

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public Mono<Customer> getCustomer(@PathVariable("customerId") String customerId) {
        return customerService.getCustomer(customerId);
    }

}
