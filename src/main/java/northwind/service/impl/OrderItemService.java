package northwind.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import northwind.model.Product;
import northwind.service.api.IProductService;
import northwind.util.NorthwindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import northwind.client.ReactiveHttpClient;
import northwind.exception.CoreException;
import northwind.model.OrderItems;
import northwind.service.api.IOrderItemService;
import northwind.util.OrderItemExtractor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;


@Service
public class OrderItemService implements IOrderItemService {

	@Autowired
	Scheduler schedular;
	
	@Autowired
	ReactiveHttpClient httpClient;

	@Autowired
	IProductService productService ;
	
	@Override
	public Mono<List<OrderItems>> getOrderItems(int orderId) {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$filter", "OrderID eq "+orderId);
		return Mono.create((emitter)->{
		try {
			Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL,"Order_Details", HttpMethod.GET, headers, queryParams,null);
			response.subscribeOn(schedular);
			List<Mono<Product>> productsMono = new ArrayList<>();
			response.subscribe((ClientResponse clientResponse)->{
				clientResponse.bodyToMono(String.class).subscribe((json) -> {
					List<OrderItems> orderItems = OrderItemExtractor.extractOrderItems(json);
					for (OrderItems item : orderItems) {
						productsMono.add(productService.getProduct(item.getProduct().getId()));
					}
					Mono.zip(productsMono, (productsArray) -> {
						return productsArray;
					}).subscribe((productsArray) -> {
						List<Product> products = Arrays.stream(productsArray)
								.filter((product) -> product instanceof Product)
								.map((product) -> (Product) product)
								.collect(Collectors.toList());
						for (OrderItems item : orderItems) {
							Product correspondingProduct = products.stream().filter((product) -> {
								return product.getId() == item.getProduct().getId();
							}).findFirst().get();
							item.setProduct(correspondingProduct);
						}
						emitter.success(orderItems);
					});

				});

			});
		} catch (CoreException e) {
			e.printStackTrace();
			emitter.error(e);
		}
		});
	}

}
