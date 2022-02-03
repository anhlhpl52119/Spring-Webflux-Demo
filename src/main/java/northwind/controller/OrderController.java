package northwind.controller;

import northwind.model.Order;
import northwind.service.api.IOrderService;
import northwind.util.OrderIdsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class OrderController {

    @Autowired
    private OrderIdsCache orderIdsCache;

    @Autowired
    private IOrderService orderService;

    @RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
    public Mono<Order> getOrder(@PathVariable("orderId") int orderId) {
        return orderService.getOrder(orderId);
    }
//this using mapping handling
    @GetMapping(path = "/order-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Order>> getOrderStream() {
        Flux<ServerSentEvent<Order>> sseOrderFlux = Flux.fromIterable(orderIdsCache.getOrderIds())
                .delayElements(Duration.ofSeconds(5)).flatMap(orderId -> orderService.getOrder(orderId)).map(order -> {
                    ServerSentEvent<Order> sseOrder = ServerSentEvent.<Order>builder().id(String.valueOf(order.getId()))
                            .event("order-stream-event").data(order).build();
                    return sseOrder;

                });
        return sseOrderFlux;
    }
}
