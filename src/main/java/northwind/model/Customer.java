package northwind.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Customer {

	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private Address address;

}
