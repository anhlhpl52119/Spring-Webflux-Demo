package northwind.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

	private int id;

	private Customer customer;

	private Address address;

	private LocalDateTime orderDate;

	private LocalDateTime shippedDate;

	private LocalDateTime requiredDate;

	private double shippingFee;

	private double taxes;

	private String paymentType;

	private List<OrderItems> orderItems;

}
