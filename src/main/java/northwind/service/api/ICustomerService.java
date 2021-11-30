package northwind.service.api;

import northwind.model.Customer;
import reactor.core.publisher.Mono;

public interface ICustomerService {
	Mono<Customer> getCustomer(String customerId);
}
