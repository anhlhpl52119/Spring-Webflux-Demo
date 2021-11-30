package northwind.service.api;

import northwind.model.Product;
import reactor.core.publisher.Mono;

public interface IProductService {
	 Mono<Product> getProduct(int productId);
}
