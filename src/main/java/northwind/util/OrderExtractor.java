package northwind.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.model.Address;
import northwind.model.Order;

/*
{
"OrderID": 10248,
"CustomerID": "VINET",
"EmployeeID": 5,
"OrderDate": "1996-07-04T00:00:00",
"RequiredDate": "1996-08-01T00:00:00",
"ShippedDate": "1996-07-16T00:00:00",
"ShipVia": 3,
"Freight": "32.3800",
"ShipName": "Vins et alcools Chevalier",
"ShipAddress": "59 rue de l'Abbaye",
"ShipCity": "Reims",
"ShipRegion": null,
"ShipPostalCode": "51100",
"ShipCountry": "France"
}
*/
public class OrderExtractor {
	
	public static Order extractOrder(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode orders = (ArrayNode) node;
				Order order = StreamSupport.stream(orders.spliterator(), false).map((orderNode) -> {
					Order o = new Order();
					o.setRequiredDate(parse(orderNode.get("RequiredDate").asText()));
					o.setShippedDate(parse(orderNode.get("ShippedDate").asText()));
					o.setOrderDate(parse(orderNode.get("OrderDate").asText()));
					o.setId(orderNode.get("OrderID").asInt());
					o.setAddress(extractShipperAddress(orderNode));
					return o;
				}).findFirst().orElseThrow(() -> new IOException());
				return order;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Address extractShipperAddress(JsonNode addressNode) {
		Address address = new Address();
		address.setAddress(addressNode.get("ShipAddress").asText());
		address.setCity(addressNode.get("ShipCity").asText());
		address.setCountry(addressNode.get("ShipCountry").asText());
		address.setPhone(null);
		address.setState(addressNode.get("ShipRegion").asText());
		address.setZip(addressNode.get("ShipPostalCode").asText());
		return address;
	}

	public static LocalDateTime parse(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return LocalDateTime.parse(dateStr, formatter);
	}

	public static List<Integer> extractOrderIds(String jsonString){
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode orders = (ArrayNode) node;
				List<Integer> orderIds = StreamSupport.stream(orders.spliterator(), false).map((orderNode) -> {
					return orderNode.get("OrderID").asInt();
				}).collect(Collectors.toList());
				return orderIds;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
