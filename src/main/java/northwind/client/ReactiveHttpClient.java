package northwind.client;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriBuilder;

import northwind.exception.CoreException;
import reactor.core.publisher.Mono;

@Component
public class ReactiveHttpClient {
	final static Logger logger = Logger.getLogger(ReactiveHttpClient.class);

	
	public Mono<ClientResponse> request(final String url, final String entity, final HttpMethod method,
			Map<String, String> headers, Map<String, String> queryParams, final String jsonString)
			throws CoreException {

		WebClient client = WebClient.builder().baseUrl(url)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
		client.method(HttpMethod.GET);

		RequestHeadersUriSpec request = client.get();

		headers.forEach((key, value) -> {
			request.header(key, value);
		});
		Function<UriBuilder, URI> func = (UriBuilder uriBuilder) -> {
			uriBuilder.path(entity).queryParam("$format", "json");
			queryParams.forEach((key, value) -> {
				uriBuilder.queryParam(key, value);
			});
			URI uri = uriBuilder.build();
			System.out.println(uri);
			return uri;
		};
		return request.uri(func).exchange();

	}

}
