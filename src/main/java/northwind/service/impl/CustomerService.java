package northwind.service.impl;

import java.util.HashMap;
import java.util.Map;

import northwind.util.NorthwindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import northwind.client.ReactiveHttpClient;
import northwind.exception.CoreException;
import northwind.model.Customer;
import northwind.service.api.ICustomerService;
import northwind.util.CustomerExtractor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class CustomerService implements ICustomerService {
	
	@Autowired
	Scheduler schedular;
	
	@Autowired
	ReactiveHttpClient httpClient;
	
	public Mono<Customer> getCustomer(String customerId) {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$filter", "CustomerID eq '"+customerId+"'");
		return Mono.create((emitter)->{
		try {
			Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL,"Customers", HttpMethod.GET, headers, queryParams,null);
			System.out.println("Non blocking");
			response.subscribeOn(schedular);
			response.subscribe((ClientResponse clientResponse)->{
				clientResponse.bodyToMono(String.class).subscribe((json) -> {
					Customer customer = CustomerExtractor.extractCustomer(json);
					emitter.success(customer);
				});

			});
			
		} catch (CoreException e) {
			e.printStackTrace();
			emitter.error(e);
		}
		});

	}
}
