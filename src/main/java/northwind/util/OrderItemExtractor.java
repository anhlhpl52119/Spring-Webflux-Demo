package northwind.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.model.OrderItems;
import northwind.model.Product;

public class OrderItemExtractor {
	public static List<OrderItems> extractOrderItems(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode arrayNode = parent.get("value");
			if (arrayNode instanceof ArrayNode) {
				ArrayNode orderItemsNode = (ArrayNode) arrayNode;
				List<OrderItems> orderItems = StreamSupport.stream(orderItemsNode.spliterator(), false).map((node) -> {
					OrderItems oi = new OrderItems();
					oi.setOrderId(node.get("OrderID").asInt());
					Product p = new Product();
					p.setId(node.get("ProductID").asInt());
					oi.setProduct(p);
					oi.setPrice(node.get("UnitPrice").asDouble());
					oi.setQuantity(node.get("Quantity").asInt());
					oi.setDiscount(node.get("Discount").asDouble());
					return oi;
				}).collect(Collectors.toList());
				return orderItems;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
