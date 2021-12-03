package northwind.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderItems {

	private int orderId;

	private Product product;
	
	private double quantity;

	private double price;
	
	private double discount;
	
}
