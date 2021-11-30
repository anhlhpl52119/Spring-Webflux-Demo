package northwind.util;

import java.io.IOException;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.model.Address;
import northwind.model.Customer;


public class CustomerExtractor {

	public static Customer extractCustomer(String jsonString) {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode node = parent.get("value");
			if (node instanceof ArrayNode) {
				ArrayNode customers = (ArrayNode) node;
				Customer customer = StreamSupport.stream(customers.spliterator(), false).map((customerNode) -> {
					String[] nameParts = customerNode.get("ContactName").asText().split(" ");
					Customer c = new Customer();
					c.setFirstName(nameParts[0]);
					c.setLastName(nameParts[nameParts.length-1]);
					c.setId(customerNode.get("CustomerID").asText());
					c.setAddress(extractCustomerAddress(customerNode));
					return c;
				}).findFirst().orElseThrow(() -> new IOException());
				return customer;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Address extractCustomerAddress(JsonNode addressNode) {
		Address address = new Address();
		address.setAddress(addressNode.get("Address").asText());
		address.setCity(addressNode.get("City").asText());
		address.setCountry(addressNode.get("Country").asText());
		address.setPhone(addressNode.get("Phone").asText());
		address.setState(null);
		address.setZip(addressNode.get("PostalCode").asText());
		return address;
	}
}
