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
import northwind.model.Product;
import northwind.service.api.IProductService;
import northwind.util.ProductExtractor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class ProductService implements IProductService {

	@Autowired
	Scheduler schedular;
	
	@Autowired
	ReactiveHttpClient httpClient;
	
	@Override
	public Mono<Product> getProduct(int productId) {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$filter", "ProductID eq "+productId);
		return Mono.create((emitter)->{
		try {
			Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL,"Products", HttpMethod.GET, headers, queryParams,null);
			response.subscribeOn(schedular);
			response.subscribe((ClientResponse clientResponse)->{
				clientResponse.bodyToMono(String.class).subscribe((json) -> {
					Product product =ProductExtractor.extractProduct(json);
					emitter.success(product);
				});

			});
		} catch (CoreException e) {
			e.printStackTrace();
			emitter.error(e);
		}
		});
		
	}

}
