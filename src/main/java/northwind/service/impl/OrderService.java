package northwind.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

import northwind.util.NorthwindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.client.ReactiveHttpClient;
import northwind.exception.CoreException;
import northwind.model.Customer;
import northwind.model.Order;
import northwind.model.OrderItems;
import northwind.service.api.ICustomerService;
import northwind.service.api.IOrderItemService;
import northwind.service.api.IOrderService;
import northwind.service.api.IProductService;
import northwind.util.OrderExtractor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;

@Service
public class OrderService implements IOrderService {

	@Autowired
	Scheduler schedular;

	@Autowired
	ReactiveHttpClient httpClient;

	@Autowired
	IProductService productService;

	@Autowired
	ICustomerService customerService;

	@Autowired
	IOrderItemService orderItemsSvc;

	@Override
	public Mono<List<Integer>> getOrderIds() {
		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$select", "OrderID");
		return Mono.create((emitter) -> {
			try {
				Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL, "Orders", HttpMethod.GET, headers,
						queryParams, null);
				response.subscribeOn(schedular);
				response.subscribe((ClientResponse clientResponse) -> {
					clientResponse.bodyToMono(String.class).subscribe((json) -> {
						List<Integer> orderIds = OrderExtractor.extractOrderIds(json);
						emitter.success(orderIds);
					});

				});
			} catch (CoreException e) {
				e.printStackTrace();
				emitter.error(e);
			}
		});
	}

	@Override
	public Mono<Order> getOrder(int orderID) {

		Map<String, String> headers = new HashMap<>();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("$filter", "OrderID eq " + orderID);

		return Mono.create((emitter) -> {
			try {
				Mono<List<OrderItems>> oiMono = orderItemsSvc.getOrderItems(orderID);
				Mono<ClientResponse> response = httpClient.request(NorthwindUtil.URL, "Orders", HttpMethod.GET, headers,
						queryParams, null);
				System.out.println("Non blocking");
				response.subscribeOn(schedular);
				response.subscribe((ClientResponse clientResponse) -> {
					clientResponse.bodyToMono(String.class).subscribe((json) -> {
						Mono<Customer> customerMono = customerService.getCustomer(extractCustomerID(json));

						Order order = OrderExtractor.extractOrder(json);
						Mono<Tuple2<Customer, List<OrderItems>>> tuple = Mono.zip(customerMono, oiMono);
						tuple.subscribe((t) -> {
							Customer customer = t.getT1();
							List<OrderItems> orderItems = t.getT2();
							order.setCustomer(customer);
							order.setOrderItems(orderItems);
							emitter.success(order);
						});

					});

				});
			} catch (CoreException e) {
				e.printStackTrace();
				emitter.error(e);
			}
		});

	}

	private String extractCustomerID(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode orders = (ArrayNode) node;
				return StreamSupport.stream(orders.spliterator(), false).map((orderNode) -> {
					return orderNode.get("CustomerID").asText();
				}).findFirst().orElseThrow(() -> new IOException());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
