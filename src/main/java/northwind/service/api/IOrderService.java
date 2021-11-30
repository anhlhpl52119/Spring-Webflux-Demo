package northwind.service.api;

import northwind.model.Order;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IOrderService {
	Mono<Order> getOrder(int orderID);

	Mono<List<Integer>> getOrderIds();
}
