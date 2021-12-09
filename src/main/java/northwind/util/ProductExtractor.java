package northwind.util;

import java.io.IOException;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.model.Product;

public class ProductExtractor {

	public static Product extractProduct(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode productsNode = parent.get("value");
			if (productsNode instanceof ArrayNode) {
				ArrayNode products = (ArrayNode) productsNode;
				Product product = StreamSupport.stream(products.spliterator(), false).map((node) -> {
					Product p = new Product();
					p.setId(node.get("ProductID").asInt());
					p.setName(node.get("ProductName").asText());
					p.setDiscontinued(node.get("Discontinued").asBoolean());
					p.setQuantityPerUnit(node.get("QuantityPerUnit").asText());
					p.setUnitPrice(Double.parseDouble(node.get("UnitPrice").asText()));
					return p;
				}).findFirst().orElseThrow(() -> new IOException());
				return product;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
