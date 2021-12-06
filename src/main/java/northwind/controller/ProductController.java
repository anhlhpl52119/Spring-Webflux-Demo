package northwind.controller;

import northwind.model.Product;
import northwind.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProductController {

    @Autowired
    IProductService productService;

    @RequestMapping(path = "/product/{productId}", method = RequestMethod.GET)
    public Mono<Product> getProduct(@PathVariable("productId") int productId) {
        return productService.getProduct(productId);
    }
}
