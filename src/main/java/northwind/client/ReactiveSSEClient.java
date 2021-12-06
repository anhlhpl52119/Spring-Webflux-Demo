package northwind.client;

import northwind.model.Order;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ReactiveSSEClient {

    public Flux<ServerSentEvent<Order>> getServerSentOrders() {
        WebClient client = WebClient.create("http://localhost:8080/");
        ParameterizedTypeReference<ServerSentEvent<Order>> type = new ParameterizedTypeReference<ServerSentEvent<Order>>() {
        };

        Flux<ServerSentEvent<Order>> eventStream = client.get()
                .uri("/order-stream")
                .retrieve()
                .bodyToFlux(type);

        return eventStream;
    }
}
