package northwind.service.api;

import java.util.List;

import northwind.model.OrderItems;
import reactor.core.publisher.Mono;

public interface IOrderItemService {
	Mono<List<OrderItems>> getOrderItems(int orderId);
}
