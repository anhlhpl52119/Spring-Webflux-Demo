package northwind.util;

import java.io.IOException;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import northwind.model.Product;
/*
{
	"value": [
	{
	"ProductID": 1,
	"ProductName": "Chai",
	"SupplierID": 1,
	"CategoryID": 1,
	"QuantityPerUnit": "10 boxes x 20 bags",
	"UnitPrice": "18.0000",
	"UnitsInStock": 39,
	"UnitsOnOrder": 0,
	"ReorderLevel": 10,
	"Discontinued": false
	}
	]
}
*/
public class ProductExtractor {

	public static Product extractProduct(String jsonString)  {
		JsonNode parent;
		try {
			parent = new ObjectMapper().readTree(jsonString);
			JsonNode productsNode = parent.get("value");
			if (productsNode instanceof ArrayNode) {
				ArrayNode products = (ArrayNode) productsNode;
				Product product = StreamSupport.stream(products.spliterator(), false).map((node) -> {
					Product  p = new Product();
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
